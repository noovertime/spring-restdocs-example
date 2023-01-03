package com.noovertime.ex.restdocs.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JsonUtil {

  private static final ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    // 필요한 것 추가
    objectMapper.registerModule(module);
  }

  private JsonUtil() {}

  public static String write(Object obj) throws JsonProcessingException {
    if (obj == null) {
      return null;
    }

    return objectMapper.writeValueAsString(obj);
  }
}
