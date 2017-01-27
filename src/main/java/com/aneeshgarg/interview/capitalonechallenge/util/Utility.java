package com.aneeshgarg.interview.capitalonechallenge.util;

import com.aneeshgarg.interview.capitalonechallenge.response.BaseResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Aneesh Garg
 */
public class Utility {

    private static final DateFormat displayDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static final String TRANSACTION_PRINT_FORMAT = "%30s%30s%10s%10s";
    public static final TransactionDateComparator transactionDateComparator = new TransactionDateComparator();

    private static final String NO_ERROR = "no-error";
    private static final String DOLLAR_FORMAT = "$%.2f";

    public static String getDisplayDateTime(Date date) {
        return displayDateFormat.format(date);
    }

    public static String getDisplayAmountInDollars(long amount) {
        return String.format(DOLLAR_FORMAT, (amount / 10000.0));
    }

    public static String getDisplayAmountInDollars(double amount) {
        return String.format(DOLLAR_FORMAT, (amount / 10000.0));
    }

    public static boolean isNoErrorResposne(BaseResponse response) {
        return response != null && NO_ERROR.equalsIgnoreCase(response.getError());
    }
}
