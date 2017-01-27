package com.aneeshgarg.interview.capitalonechallenge.service.impl;

import com.aneeshgarg.interview.capitalonechallenge.request.LoginRequest;
import com.aneeshgarg.interview.capitalonechallenge.response.LoginResponse;
import com.aneeshgarg.interview.capitalonechallenge.service.LoginService;
import com.aneeshgarg.interview.capitalonechallenge.service.RestClientService;
import com.aneeshgarg.interview.capitalonechallenge.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Aneesh Garg
 */
@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    private static final String LOGIN_PATH="login";

    @Autowired
    RestClientService restClientService;

    @Override
    public LoginResponse login(String username, String password) {
        LoginRequest request = new LoginRequest();
        request.setEmail(username);
        request.setPassword(password);
        request.setArgs(restClientService.getCommonArguments(0,null));

        LoginResponse response = (LoginResponse) restClientService.postForObject(LOGIN_PATH,request, LoginRequest.class, LoginResponse.class);
        if (Utility.isNoErrorResposne(response)) {
            return response;
        }
        else if (response != null){
            logger.error("Error in logging in user {}. Error: {}", username, response.getError());
        }
        return response;
    }
}
