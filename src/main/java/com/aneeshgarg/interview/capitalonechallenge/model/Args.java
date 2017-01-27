package com.aneeshgarg.interview.capitalonechallenge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Common Arguments
 * @author Aneesh Garg
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Args implements Serializable {

    @JsonProperty("uid")
    private long uid;

    @JsonProperty("token")
    private String token;

    @JsonProperty("api-token")
    private String apiToken;

    @JsonProperty("json-strict-mode")
    private boolean jsonStrictMode;

    @JsonProperty("json-verbose-response")
    private boolean jsonVerboseResponse;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public boolean isJsonStrictMode() {
        return jsonStrictMode;
    }

    public void setJsonStrictMode(boolean jsonStrictMode) {
        this.jsonStrictMode = jsonStrictMode;
    }

    public boolean isJsonVerboseResponse() {
        return jsonVerboseResponse;
    }

    public void setJsonVerboseResponse(boolean jsonVerboseResponse) {
        this.jsonVerboseResponse = jsonVerboseResponse;
    }

    @Override
    public String toString() {
        return "Args{" +
                "uid=" + uid +
                ", token='" + token + '\'' +
                ", apiToken='" + apiToken + '\'' +
                ", jsonStrictMode=" + jsonStrictMode +
                ", jsonVerboseResponse=" + jsonVerboseResponse +
                '}';
    }
}
