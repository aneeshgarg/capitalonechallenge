package com.aneeshgarg.interview.capitalonechallenge.service.impl;

import com.aneeshgarg.interview.capitalonechallenge.model.Args;
import com.aneeshgarg.interview.capitalonechallenge.service.RestClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author Aneesh Garg
 */
@Service
public class RestClientServiceImpl implements RestClientService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private static final String API_TOKEN = "AppTokenForInterview";

    private static final String BASE_URL= "https://2016.api.levelmoney.com/api/v2/core/";

    private static final RestTemplate restTemplate = new RestTemplate();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Args getCommonArguments(long uid, String token) {
        Args args = new Args();
        args.setUid(uid);
        args.setToken(token);
        args.setApiToken(API_TOKEN);
        return args;
    }

    public Object postForObject(String path, Object request, Class requestType, Class responseType) {
        String response = restTemplate.postForObject(BASE_URL + path, requestType.cast(request), String.class);
        try {
            return objectMapper.readValue(response, responseType);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
