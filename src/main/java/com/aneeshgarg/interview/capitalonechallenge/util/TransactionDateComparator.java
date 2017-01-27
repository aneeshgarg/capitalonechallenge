package com.aneeshgarg.interview.capitalonechallenge.util;

import com.aneeshgarg.interview.capitalonechallenge.model.Transaction;

import java.util.Comparator;

/**
 * @author Aneesh Garg
 */
public class TransactionDateComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        return t1.getTransactionTime().compareTo(t2.getTransactionTime());
    }
}
