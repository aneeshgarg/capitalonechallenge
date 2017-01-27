package com.aneeshgarg.interview.capitalonechallenge.model;

import com.aneeshgarg.interview.capitalonechallenge.util.Utility;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This class is a holder for objects required for generating summary report.
 *
 * @author Aneesh Garg
 */
public class SummaryPlaceholder {

    /**
     * This map contains all transactions grouped by month
     * Key - Month
     * Value - List of transactions for that month
     */
    Map<String, Set<Transaction>> groupedTransactions;

    /**
     * This map contains all spending grouped by month
     * Key - Month
     * Value - Total Spending for that month
     */
    Map<String, Long> spendSummary;

    /**
     * This map contains all income grouped by month
     * Key - Month
     * Value - Total Income for that month
     */
    Map<String, Long> incomeSummary;

    /**
     * If donut transactions are ignored then this set will contain transactions that were ignored otherwise null.
     */
    Set<Transaction> donutTransactions;

    /**
     * If credit card payments are ignored then this set will contain transactions that were ignored otherwise null.
     */
    Set<Transaction> ccPaymentTransactions;

    /**
     * If crystal ball payments are included then this set will contain transactions that were projected.
     */
    Set<Transaction> pendingTransactions;

    /**
     * All transactions that are projected.
     */
    Set<Transaction> projectedTransactions;

    public SummaryPlaceholder(boolean ignoreDonuts, boolean ignoreCCPayments, boolean crystalBall) {
        groupedTransactions = new TreeMap<>();
        spendSummary = new TreeMap<>();
        incomeSummary = new TreeMap<>();
        pendingTransactions = new HashSet<>();

        if (ignoreDonuts) {
            donutTransactions = new TreeSet<>(Utility.transactionDateComparator);
        }

        if (ignoreCCPayments) {
            ccPaymentTransactions = new TreeSet<>(Utility.transactionDateComparator);
        }

        if (crystalBall) {
            projectedTransactions = new TreeSet<>(Utility.transactionDateComparator);
        }

    }

    public Map<String, Set<Transaction>> getGroupedTransactions() {
        return groupedTransactions;
    }

    public void setGroupedTransactions(Map<String, Set<Transaction>> groupedTransactions) {
        this.groupedTransactions = groupedTransactions;
    }

    public Map<String, Long> getSpendSummary() {
        return spendSummary;
    }

    public void setSpendSummary(Map<String, Long> spendSummary) {
        this.spendSummary = spendSummary;
    }

    public Map<String, Long> getIncomeSummary() {
        return incomeSummary;
    }

    public void setIncomeSummary(Map<String, Long> incomeSummary) {
        this.incomeSummary = incomeSummary;
    }

    public Set<Transaction> getDonutTransactions() {
        return donutTransactions;
    }

    public void setDonutTransactions(Set<Transaction> donutTransactions) {
        this.donutTransactions = donutTransactions;
    }

    public Set<Transaction> getCcPaymentTransactions() {
        return ccPaymentTransactions;
    }

    public void setCcPaymentTransactions(Set<Transaction> ccPaymentTransactions) {
        this.ccPaymentTransactions = ccPaymentTransactions;
    }

    public Set<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }

    public void setPendingTransactions(Set<Transaction> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }

    public Set<Transaction> getProjectedTransactions() {
        return projectedTransactions;
    }

    public void setProjectedTransactions(Set<Transaction> projectedTransactions) {
        this.projectedTransactions = projectedTransactions;
    }

    @Override
    public String toString() {
        return "SummaryPlaceholder{" +
                "groupedTransactions=" + groupedTransactions +
                ", spendSummary=" + spendSummary +
                ", incomeSummary=" + incomeSummary +
                ", donutTransactions=" + donutTransactions +
                ", ccPaymentTransactions=" + ccPaymentTransactions +
                '}';
    }
}
