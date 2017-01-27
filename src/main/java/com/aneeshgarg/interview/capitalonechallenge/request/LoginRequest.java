package com.aneeshgarg.interview.capitalonechallenge.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Aneesh Garg
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest extends BaseRequest {

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
