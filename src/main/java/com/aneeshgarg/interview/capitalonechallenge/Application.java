package com.aneeshgarg.interview.capitalonechallenge;

import com.aneeshgarg.interview.capitalonechallenge.model.SummaryPlaceholder;
import com.aneeshgarg.interview.capitalonechallenge.model.Transaction;
import com.aneeshgarg.interview.capitalonechallenge.response.LoginResponse;
import com.aneeshgarg.interview.capitalonechallenge.service.LoginService;
import com.aneeshgarg.interview.capitalonechallenge.service.TransactionService;
import com.aneeshgarg.interview.capitalonechallenge.service.impl.LoginServiceImpl;
import com.aneeshgarg.interview.capitalonechallenge.service.impl.TransactionServiceImpl;
import com.aneeshgarg.interview.capitalonechallenge.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static final String USERNAME_OPTION = "--username";
    private static final String PASSWORD_OPTION = "--password";
    private static final String IGNORE_DONUTS_OPTION = "--ignore-donuts";
    private static final String IGNORE_CC_PAYMENTS_OPTION = "--ignore-cc-payments";
    private static final String CRYSTAL_BALL_OPTION = "--crystal-ball";

    public static void main(String[] args) {

        logger.info("Arguments: " + Arrays.asList(args));
        ApplicationContext context = SpringApplication.run(Application.class);

        String username = null;
        String password = null;
        boolean ignoreDonuts = false;
        boolean ignoreCCPayments = false;
        boolean crystalBall = false;

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case USERNAME_OPTION:
                        if ((i + 1) < args.length) {
                            username = args[i + 1];
                            i = i + 1;
                            logger.info("Username Provided: " + username);
                        } else {
                            logger.error("Insufficient arguments for username. Exiting Application.");
                            return;
                        }
                        break;
                    case PASSWORD_OPTION:
                        if ((i + 1) < args.length) {
                            password = args[i + 1];
                            i = i + 1;
                            logger.info("Password Provided. ");
                        } else {
                            logger.error("Insufficient arguments for password. Exiting Application.");
                            return;
                        }
                        break;
                    case IGNORE_DONUTS_OPTION:
                        ignoreDonuts = true;
                        logger.info("Ignoring donuts.");
                        break;
                    case IGNORE_CC_PAYMENTS_OPTION:
                        ignoreCCPayments = true;
                        logger.info("Ignoring credit card payments.");
                        break;
                    case CRYSTAL_BALL_OPTION:
                        crystalBall = true;
                        logger.info("Predicting current month transactions.");
                        break;
                }
            }
        }
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            logger.warn("Either username or password not provided. Using default user: interview@levelmoney.com");
            username = "interview@levelmoney.com";
            password = "password2";
        }

        LoginService loginService = context.getBean(LoginServiceImpl.class);
        LoginResponse loginResponse = loginService.login(username, password);
        if (Utility.isNoErrorResposne(loginResponse)) {
            logger.info("{} is logged in.", username);
            TransactionService transactionService = context.getBean(TransactionServiceImpl.class);
            SummaryPlaceholder summaryPlaceholder = transactionService.getTransactionSummary(loginResponse.getUid(), loginResponse.getToken(), ignoreDonuts, ignoreCCPayments, crystalBall);
            printSummary(summaryPlaceholder, ignoreDonuts, ignoreCCPayments, crystalBall);
        } else {
            logger.error("There was error in logging in.");
        }
    }

    private static void printSummary(SummaryPlaceholder summaryPlaceholder, boolean ignoreDonuts, boolean ignoreCCPayments, boolean crystalBall) {
        if (summaryPlaceholder != null) {
            // Printing all pending transactions
            printTransactions("IGNORED PENDING TRANSACTIONS", summaryPlaceholder.getPendingTransactions());

            // Printing monthly summary and average
            printMonthlySummaryAndAverage(summaryPlaceholder.getSpendSummary(), summaryPlaceholder.getIncomeSummary());

            if (ignoreCCPayments) {
                printTransactions("IGNORED CC PAYMENTS", summaryPlaceholder.getCcPaymentTransactions());
            }

            if (ignoreDonuts) {
                printTransactions("IGNORED DONUTS", summaryPlaceholder.getDonutTransactions());
            }

            if (crystalBall) {
                printTransactions("PROJECTED TRANSACTIONS", summaryPlaceholder.getProjectedTransactions());
            }

        } else {
            logger.warn("No Transactions to print.");
        }
    }

    private static void printMonthlySummaryAndAverage(Map<String, Long> spendSummary, Map<String, Long> incomeSummary) {
        if (!CollectionUtils.isEmpty(spendSummary) || !CollectionUtils.isEmpty(incomeSummary)) {

            // There is a possibility that there may be a month with no spending but with some income and vice versa
            // Therefore creating comprehensive set of months
            Set<String> keySet = new TreeSet<>();
            // Adding all months where there was spending
            if (!CollectionUtils.isEmpty(spendSummary)) {
                keySet.addAll(spendSummary.keySet());
            }
            // Adding all months where there was income
            if (!CollectionUtils.isEmpty(incomeSummary)) {
                keySet.addAll(incomeSummary.keySet());
            }

            long totalSpending = 0;
            long totalIncome = 0;

            logger.info("*********************************** TRANSACTION SUMMARY ***********************************");
            logger.info("{}{}{}", String.format("%10s", "Month"), String.format("%10s", "Spent"), String.format("%10s", "Income"));
            for (String key : keySet) {
                String spent = "$0.00";
                String income = "$0.00";
                if (spendSummary.containsKey(key)) {
                    long amount = spendSummary.get(key);
                    spent = Utility.getDisplayAmountInDollars(amount);
                    totalSpending += amount;
                }
                if (incomeSummary.containsKey(key)) {
                    long amount = incomeSummary.get(key);
                    income = Utility.getDisplayAmountInDollars(amount);
                    totalIncome += amount;
                }
                logger.info(String.format("%10s%10s%10s", key, spent, income));
            }
            double averageSpent = totalSpending * 1.0 / keySet.size();
            double averageIncome = totalIncome * 1.0 / keySet.size();
            logger.info(String.format("%10s%10s%10s", "Average", Utility.getDisplayAmountInDollars(averageSpent), Utility.getDisplayAmountInDollars(averageIncome)));
        } else {
            logger.warn("No summary  to show.");
        }
    }

    private static void printTransactions(String title, Set<Transaction> pendingTransactions) {
        if (!CollectionUtils.isEmpty(pendingTransactions)) {
            logger.info("*********************************** {} ***********************************", title);
            logger.info(String.format(Utility.TRANSACTION_PRINT_FORMAT, "Date", "Merchant", "Status", "Amount"));
            for (Transaction transaction : pendingTransactions) {
                transaction.print();
            }
        }
    }

}