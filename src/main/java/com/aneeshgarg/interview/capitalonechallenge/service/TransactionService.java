package com.aneeshgarg.interview.capitalonechallenge.service;

import com.aneeshgarg.interview.capitalonechallenge.model.SummaryPlaceholder;

/**
 * @author Aneesh Garg
 */
public interface TransactionService {
    SummaryPlaceholder getTransactionSummary(long uid, String token, boolean ignoreDonuts, boolean ignoreCCPayments, boolean crystalBall);
}
