package com.aneeshgarg.interview.capitalonechallenge.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Aneesh Garg
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetProjectedTransactionsForMonthRequest extends BaseRequest {

    @JsonProperty("year")
    private long year;

    @JsonProperty("month")
    private long month;

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "GetProjectedTransactionsForMonthRequest{" +
                "year=" + year +
                ", month=" + month +
                '}';
    }
}
