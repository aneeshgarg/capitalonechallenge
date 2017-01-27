package com.aneeshgarg.interview.capitalonechallenge.model;

import com.aneeshgarg.interview.capitalonechallenge.util.ClearDateDeserializer;
import com.aneeshgarg.interview.capitalonechallenge.util.Utility;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Aneesh Garg
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

    @JsonProperty("transaction-id")
    private String transactionId;

    @JsonProperty("account-id")
    private String accountId;

    @JsonProperty("raw-merchant")
    private String rawMerchant;

    @JsonProperty("merchant")
    private String merchant;

    @JsonProperty("is-pending")
    private boolean isPending;

    @JsonProperty("transaction-time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date transactionTime;

    @JsonProperty("amount")
    private long amount;

    @JsonProperty("previous-transaction-id")
    private String previousTransactionId;

    @JsonProperty("categorization")
    private String categorization;

    @JsonProperty("clear-date")
    @JsonDeserialize(using = ClearDateDeserializer.class)
    private Date clearDate;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getRawMerchant() {
        return rawMerchant;
    }

    public void setRawMerchant(String rawMerchant) {
        this.rawMerchant = rawMerchant;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getPreviousTransactionId() {
        return previousTransactionId;
    }

    public void setPreviousTransactionId(String previousTransactionId) {
        this.previousTransactionId = previousTransactionId;
    }

    public String getCategorization() {
        return categorization;
    }

    public void setCategorization(String categorization) {
        this.categorization = categorization;
    }

    public Date getClearDate() {
        return clearDate;
    }

    public void setClearDate(Date clearDate) {
        this.clearDate = clearDate;
    }

    /**
     * This method logs string that needs to displayed to user for a transaction
     */
    public void print() {
        String status = isPending ? "Pending" : "Cleared";
        logger.info(String.format(Utility.TRANSACTION_PRINT_FORMAT, Utility.getDisplayDateTime(transactionTime), merchant, status, Utility.getDisplayAmountInDollars(amount)));
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", rawMerchant='" + rawMerchant + '\'' +
                ", merchant='" + merchant + '\'' +
                ", isPending=" + isPending +
                ", transactionTime=" + transactionTime +
                ", amount=" + amount +
                ", previousTransactionId='" + previousTransactionId + '\'' +
                ", categorization='" + categorization + '\'' +
                ", clearDate=" + clearDate +
                '}';
    }

    /**
     * Overriding equals. Using all fields because its not clear in API docs whether transaction id is unique or not.
     * If transactionId is unique then it is better to just use transactionId and not all fields.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (isPending != that.isPending) return false;
        if (amount != that.amount) return false;
        if (transactionId != null ? !transactionId.equals(that.transactionId) : that.transactionId != null)
            return false;
        if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) return false;
        if (rawMerchant != null ? !rawMerchant.equals(that.rawMerchant) : that.rawMerchant != null) return false;
        if (merchant != null ? !merchant.equals(that.merchant) : that.merchant != null) return false;
        if (transactionTime != null ? !transactionTime.equals(that.transactionTime) : that.transactionTime != null)
            return false;
        if (previousTransactionId != null ? !previousTransactionId.equals(that.previousTransactionId) : that.previousTransactionId != null)
            return false;
        if (categorization != null ? !categorization.equals(that.categorization) : that.categorization != null)
            return false;
        return clearDate != null ? clearDate.equals(that.clearDate) : that.clearDate == null;
    }

    @Override
    public int hashCode() {
        int result = transactionId != null ? transactionId.hashCode() : 0;
        result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
        result = 31 * result + (rawMerchant != null ? rawMerchant.hashCode() : 0);
        result = 31 * result + (merchant != null ? merchant.hashCode() : 0);
        result = 31 * result + (isPending ? 1 : 0);
        result = 31 * result + (transactionTime != null ? transactionTime.hashCode() : 0);
        result = 31 * result + (int) (amount ^ (amount >>> 32));
        result = 31 * result + (previousTransactionId != null ? previousTransactionId.hashCode() : 0);
        result = 31 * result + (categorization != null ? categorization.hashCode() : 0);
        result = 31 * result + (clearDate != null ? clearDate.hashCode() : 0);
        return result;
    }
}
