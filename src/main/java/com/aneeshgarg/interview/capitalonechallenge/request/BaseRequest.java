package com.aneeshgarg.interview.capitalonechallenge.request;

import com.aneeshgarg.interview.capitalonechallenge.model.Args;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Aneesh Garg
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseRequest implements Serializable {
    @JsonProperty("args")
    private Args args;

    public Args getArgs() {
        return args;
    }

    public void setArgs(Args args) {
        this.args = args;
    }
}
