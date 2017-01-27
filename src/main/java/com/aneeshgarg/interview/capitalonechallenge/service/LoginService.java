package com.aneeshgarg.interview.capitalonechallenge.service;

import com.aneeshgarg.interview.capitalonechallenge.response.LoginResponse;

/**
 * @author Aneesh Garg
 */
public interface LoginService {
    LoginResponse login(String username, String password);
}
