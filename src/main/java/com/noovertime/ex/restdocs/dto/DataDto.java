package com.noovertime.ex.restdocs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.ZonedDateTime;
import lombok.Data;

@Data
public class DataDto implements RootDto {

  private int id;
  private String name;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private ZonedDateTime currentAt;
}
