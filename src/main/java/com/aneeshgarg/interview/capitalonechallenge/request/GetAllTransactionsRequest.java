package com.aneeshgarg.interview.capitalonechallenge.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Aneesh Garg
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllTransactionsRequest extends BaseRequest{
}
