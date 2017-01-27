package com.aneeshgarg.interview.capitalonechallenge.service.impl;

import com.aneeshgarg.interview.capitalonechallenge.model.SummaryPlaceholder;
import com.aneeshgarg.interview.capitalonechallenge.model.Transaction;
import com.aneeshgarg.interview.capitalonechallenge.request.GetAllTransactionsRequest;
import com.aneeshgarg.interview.capitalonechallenge.request.GetProjectedTransactionsForMonthRequest;
import com.aneeshgarg.interview.capitalonechallenge.response.GetAllTransactionsResponse;
import com.aneeshgarg.interview.capitalonechallenge.response.GetProjectedTransactionsForMonthResponse;
import com.aneeshgarg.interview.capitalonechallenge.service.RestClientService;
import com.aneeshgarg.interview.capitalonechallenge.service.TransactionService;
import com.aneeshgarg.interview.capitalonechallenge.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Aneesh Garg
 */
@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private static final String GET_ALL_TRANSACTION_PATH = "get-all-transactions";
    private static final String GET_PROJECTED_TRANSACTIONS_FOR_MONTH = "projected-transactions-for-month";

    private static final String KRISPY_KREME = "Krispy Kreme Donuts";
    private static final String DUNKIN = "DUNKIN #336784";

    /**
     * No of milliseconds in a day = 24 hrs * 60 mins * 60 secs * 1000 milliseconds
     */
    private static final long MILLIES_IN_A_DAY = 86400000L;

    private static final DateFormat summaryMonthKeyFormat = new SimpleDateFormat("yyyy-MM");

    @Autowired
    private RestClientService restClientService;

    public SummaryPlaceholder getTransactionSummary(long uid, String token, boolean ignoreDonuts, boolean ignoreCCPayments, boolean crystalBall) {
        GetAllTransactionsResponse allTransactionsResponse = getAllTransactions(uid, token);
        if (Utility.isNoErrorResposne(allTransactionsResponse)) {
            SummaryPlaceholder summaryPlaceholder = new SummaryPlaceholder(ignoreDonuts, ignoreCCPayments, crystalBall);

            // using sorted transactions so as to improve performance.
            Set<Transaction> allTransactions = new TreeSet<>(Utility.transactionDateComparator);
            allTransactions.addAll(allTransactionsResponse.getTransactions());

            if (crystalBall) {
                GetProjectedTransactionsForMonthResponse projectedResponse = getProjectedTransactionsForCurrentMonth(uid, token);
                if (Utility.isNoErrorResposne(projectedResponse)) {
                    allTransactions.addAll(projectedResponse.getTransactions());
                    summaryPlaceholder.getProjectedTransactions().addAll(projectedResponse.getTransactions());
                } else {
                    logger.error("Error in getting projected transactions {}", (projectedResponse != null) ? projectedResponse.getError() : "");
                }
            }

            summarizeTransactions(summaryPlaceholder, allTransactions, ignoreDonuts, ignoreCCPayments, crystalBall);
            return summaryPlaceholder;
        } else {
            logger.error("Error in getting all transactions {}", (allTransactionsResponse != null) ? allTransactionsResponse.getError() : "");
        }
        return null;
    }

    /**
     * This method will summarize and group the transactions. It will return {@link SummaryPlaceholder} object contained all those groups and summaries.
     *
     * @param placeholder
     * @param transactions
     * @param ignoreDonuts
     * @param ignoreCCPayments
     * @param crystalBall
     * @return
     */
    private void summarizeTransactions(SummaryPlaceholder placeholder, Set<Transaction> transactions, boolean ignoreDonuts, boolean ignoreCCPayments, boolean crystalBall) {
        if (!CollectionUtils.isEmpty(transactions)) {
            if (ignoreCCPayments) {
                // get all cc payments and remove them
                Set<Transaction> ccPayments = getCCPayments(transactions);
                placeholder.setCcPaymentTransactions(ccPayments);
                transactions.removeAll(ccPayments);
            }

            for (Transaction transaction : transactions) {
                // Ignore a transaction only if crystal ball is not activated and transaction is pending
                if (!(!crystalBall && transaction.isPending())) {

                    if (ignoreDonuts && (KRISPY_KREME.equalsIgnoreCase(transaction.getMerchant()) || DUNKIN.equalsIgnoreCase(transaction.getMerchant()))) {
                        placeholder.getDonutTransactions().add(transaction);
                        continue;
                    }

                    Date transactionTime = transaction.getTransactionTime();
                    String summaryMonthKey = getSummaryMonthKey(transactionTime);

                    // Grouping Transaction
                    if (placeholder.getGroupedTransactions().containsKey(summaryMonthKey)) {
                        placeholder.getGroupedTransactions().get(summaryMonthKey).add(transaction);
                    } else {
                        Set<Transaction> set = new TreeSet<>(Utility.transactionDateComparator);
                        set.add(transaction);
                        placeholder.getGroupedTransactions().put(summaryMonthKey, set);
                    }

                    if (transaction.getAmount() < 0) {
                        // Transaction is spending. Add it to spending
                        if (placeholder.getSpendSummary().containsKey(summaryMonthKey)) {
                            Long totalSpending = placeholder.getSpendSummary().get(summaryMonthKey) + (transaction.getAmount() * -1);
                            placeholder.getSpendSummary().put(summaryMonthKey, totalSpending);
                        } else {
                            placeholder.getSpendSummary().put(summaryMonthKey, Long.valueOf(transaction.getAmount() * -1L));
                        }

                    } else {
                        // Transaction is income. Add it to income
                        if (placeholder.getIncomeSummary().containsKey(summaryMonthKey)) {
                            Long totalIncome = placeholder.getIncomeSummary().get(summaryMonthKey) + transaction.getAmount();
                            placeholder.getIncomeSummary().put(summaryMonthKey, totalIncome);
                        } else {
                            placeholder.getIncomeSummary().put(summaryMonthKey, Long.valueOf(transaction.getAmount()));
                        }
                    }
                } else {
                    placeholder.getPendingTransactions().add(transaction);
                    logger.warn("Pending Transaction Ignored");
                    transaction.print();
                }
            }
        }
    }

    /**
     * This method will return a string key in the form yyyy-MM
     *
     * @param date
     * @return
     */
    private String getSummaryMonthKey(Date date) {
        return summaryMonthKeyFormat.format(date);
    }

    private GetAllTransactionsResponse getAllTransactions(long uid, String token) {
        GetAllTransactionsRequest request = new GetAllTransactionsRequest();
        request.setArgs(restClientService.getCommonArguments(uid, token));
        logger.info("Getting All Transactions.");
        return (GetAllTransactionsResponse) restClientService.postForObject(GET_ALL_TRANSACTION_PATH, request, GetAllTransactionsRequest.class, GetAllTransactionsResponse.class);
    }

    private GetProjectedTransactionsForMonthResponse getProjectedTransactionsForCurrentMonth(long uid, String token) {
        GetProjectedTransactionsForMonthRequest request = new GetProjectedTransactionsForMonthRequest();
        request.setArgs(restClientService.getCommonArguments(uid, token));
        Calendar today = Calendar.getInstance();
        request.setYear(today.get(Calendar.YEAR));
        request.setMonth(today.get(Calendar.MONTH) + 1);
        logger.info("Getting projected transactions for {}-{}", request.getYear(), request.getMonth());
        return (GetProjectedTransactionsForMonthResponse) restClientService.postForObject(GET_PROJECTED_TRANSACTIONS_FOR_MONTH, request, GetProjectedTransactionsForMonthRequest.class, GetProjectedTransactionsForMonthResponse.class);
    }

    /**
     * This method will identify all the transactions which are credit card payments and return them as a set.
     *
     * @param transactions
     * @return
     */
    private Set<Transaction> getCCPayments(Set<Transaction> transactions) {
        logger.info("Calculating cc payments.");
        // Divide transactions by Spending and Income sorted by transaction datetime
        List<Transaction> spendingTransactions = new ArrayList<>();
        List<Transaction> incomeTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                incomeTransactions.add(transaction);
            } else {
                spendingTransactions.add(transaction);
            }
        }

        Set<Transaction> ccPayments = new TreeSet<>(Utility.transactionDateComparator);

        //Sorting these lists by transaction date time
        Collections.sort(spendingTransactions, Utility.transactionDateComparator);
        Collections.sort(incomeTransactions, Utility.transactionDateComparator);

        while (!isEitherCollectionEmpty(spendingTransactions, incomeTransactions)) {
            // Get first spending transaction
            Transaction spend = null;
            Transaction income = null;
            long timeDiff = 0;

            // Bring time diff between first spend and first income less than 24 hours
            // this  will significantly reduce the size of each set thus improving performance
            do {
                spend = spendingTransactions.get(0);
                income = incomeTransactions.get(0);
                timeDiff = getAbsTransactionTimeDiff(spend, income);
                if (timeDiff > MILLIES_IN_A_DAY) {
                    // Top two transactions are more than a day apart
                    if (spend.getTransactionTime().getTime() < income.getTransactionTime().getTime()) {
                        spendingTransactions.remove(0);
                    } else {
                        incomeTransactions.remove(0);
                    }
                }
            }
            while ((timeDiff > MILLIES_IN_A_DAY) && !isEitherCollectionEmpty(spendingTransactions, incomeTransactions));
            // Either time diff is less than a day or list is empty

            if (!isEitherCollectionEmpty(spendingTransactions, incomeTransactions)) {
                //This means time diff is less than a day
                spend = spendingTransactions.get(0);
                int incomeIndex = 0;
                // For this spending Check if there is a corresponding income within 24 hours
                do {
                    income = incomeTransactions.get(incomeIndex);
                    timeDiff = getAbsTransactionTimeDiff(spend, income);
                    if (Math.abs(spend.getAmount()) == income.getAmount()) {
                        ccPayments.add(spend);
                        ccPayments.add(income);
                        spendingTransactions.remove(spend);
                        incomeTransactions.remove(income);
                        break;
                    }
                    incomeIndex++;
                } while (timeDiff < MILLIES_IN_A_DAY && incomeIndex < incomeTransactions.size());
                // spending is not a cc payment
                spendingTransactions.remove(spend);
            }
        }
        return ccPayments;
    }

    private boolean isEitherCollectionEmpty(Collection c1, Collection c2) {
        return c1.isEmpty() || c2.isEmpty();
    }

    private long getAbsTransactionTimeDiff(Transaction t1, Transaction t2) {
        long diff = t1.getTransactionTime().getTime() - t2.getTransactionTime().getTime();
        return Math.abs(diff);
    }
}
