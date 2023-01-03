package com.noovertime.ex.restdocs;


import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@ServletComponentScan
@SpringBootApplication
public class ExRestDocsApplication {

  public static void main(String[] args) {
    // 서버 타임존 설정
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    // 추가 설정 없이 기본은 default가 아닌 local로
    System.setProperty("spring.profiles.default", "local");
    // 시작
    SpringApplication.run(ExRestDocsApplication.class, args);
  }
}
