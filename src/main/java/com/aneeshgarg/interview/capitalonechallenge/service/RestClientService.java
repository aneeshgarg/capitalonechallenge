package com.aneeshgarg.interview.capitalonechallenge.service;

import com.aneeshgarg.interview.capitalonechallenge.model.Args;

/**
 * @author Aneesh Garg
 */
public interface RestClientService {
    Args getCommonArguments(long uid, String token);

    Object postForObject(String path, Object request, Class requestType, Class responseType);
}
