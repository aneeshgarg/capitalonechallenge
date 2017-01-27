package com.aneeshgarg.interview.capitalonechallenge.response;

import com.aneeshgarg.interview.capitalonechallenge.model.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Aneesh Garg
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllTransactionsResponse extends BaseResponse {
    
    @JsonProperty("transactions")
    private List<Transaction> transactions;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "GetAllTransactionsResponse{" +
                "transactions=" + transactions +
                '}';
    }
}
