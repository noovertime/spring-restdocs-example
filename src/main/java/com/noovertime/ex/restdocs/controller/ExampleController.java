package com.noovertime.ex.restdocs.controller;

import com.noovertime.ex.restdocs.dto.DataDto;
import com.noovertime.ex.restdocs.dto.ReqDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(value = "/api/ex")
public class ExampleController {

  @GetMapping("/get/query")
  @ResponseBody
  public DataDto getQueryString(HttpServletRequest request, @RequestParam int id) {
    log.info("method = {}, requstUri = {}, queryString={}", request.getMethod(),
        request.getRequestURI(), request.getQueryString());

    DataDto dto = new DataDto();
    dto.setId(id);
    dto.setName("getQueryString");
    dto.setCurrentAt(ZonedDateTime.now());

    return dto;
  }

  @PostMapping(value = "/post/json")
  @ResponseBody
  public DataDto postJson(HttpServletRequest request, @RequestBody @Valid ReqDto reqDto) {
    DataDto dto = new DataDto();
    dto.setId(reqDto.getId());
    dto.setName("postJson");
    dto.setCurrentAt(ZonedDateTime.now());

    return dto;
  }

  @PostMapping(value = "/post/param")
  @ResponseBody
  public DataDto postParam(HttpServletRequest request, @RequestParam int id) {
    DataDto dto = new DataDto();
    dto.setId(id);
    dto.setName("postParam");
    dto.setCurrentAt(null);

    return dto;
  }

  @PostMapping(value = "/post/part")
  public void postMultipart(@RequestPart(value = "reqDto") ReqDto reqDto,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    log.debug("reqDto = {}, files.size={}", reqDto, files == null ? 0 : files.size());
  }
}
