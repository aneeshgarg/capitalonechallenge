package com.aneeshgarg.interview.capitalonechallenge.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Date;

/**
 * @author Aneesh Garg
 */
public class ClearDateDeserializer extends JsonDeserializer<Date> {
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Date date = new Date();
        date.setTime(p.getLongValue());
        return new Date();
    }
}
