package com.noovertime.ex.restdocs;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.formParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.noovertime.ex.restdocs.dto.ReqDto;
import com.noovertime.ex.restdocs.utils.JsonUtil;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

@DisplayName("ExampleControllerTest")
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class ExampleControllerTest extends AbstractTest {

  private static final String API_PATH = "/api/ex";


  @DisplayName("get + queryString")
  @Test
  void testGetQueryString() throws Exception {
    final String apiPath = API_PATH + "/get/query";
    final HttpMethod httpMethod = HttpMethod.GET;
    final String docPath = super.getDocPath(httpMethod, apiPath);

    // ?????? ??????
    MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders.get(apiPath);
    // ????????? ??????, ????????? ????????? controller?????? requst.getQueryString() ?????? ??? ??? ????????????
    //requestBuilder.param("id", "1");
    requestBuilder.queryParam("id", "1"); // request.getQueryString()?????? ??? ????????????

    // ??????
    ResultActions resultActions = this.mockMvc.perform(requestBuilder);

    // ?????? ?????? ??? ?????????
    resultActions.andDo(print())
        .andExpect(status().isOk())
        .andDo(
            document(docPath,
                super.getDocumentRequest(),
                super.getDocumentResponse(),
                queryParameters(parameterWithName("id").description("????????????")),
                createResponseFieldsSnippet()
            )
        );

  }


  @DisplayName("post + json")
  @Test
  void testPostJson() throws Exception {
    final String apiPath = API_PATH + "/post/json";
    final HttpMethod httpMethod = HttpMethod.POST;
    final String docPath = super.getDocPath(httpMethod, apiPath);

    // ?????? ??????
    ReqDto reqDto = new ReqDto();
    reqDto.setId(1);

    MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders.post(apiPath);
    requestBuilder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .content(JsonUtil.write(reqDto));

    // ??????
    ResultActions resultActions = this.mockMvc.perform(requestBuilder);

    // ?????? ?????? ??? ?????????
    resultActions.andDo(print())
        .andExpect(status().isOk())
        .andDo(
            document(docPath,
                super.getDocumentRequest(),
                super.getDocumentResponse(),
                requestFields(fieldWithPath("id").type(JsonFieldType.NUMBER).description("id")),
                createResponseFieldsSnippet()
            )
        );
  }


  @DisplayName("post + param")
  @Test
  void testPostParam() throws Exception {
    final String apiPath = API_PATH + "/post/param";
    final HttpMethod httpMethod = HttpMethod.POST;
    final String docPath = super.getDocPath(httpMethod, apiPath);

    // ?????? ??????
    MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders.post(apiPath);
    requestBuilder.param("id", "1");

    // ??????
    ResultActions resultActions = this.mockMvc.perform(requestBuilder);

    // ?????? ?????? ??? ?????????
    resultActions.andDo(print())
        .andExpect(status().isOk())
        .andDo(
            document(docPath,
                super.getDocumentRequest(),
                super.getDocumentResponse(),
                formParameters(parameterWithName("id").description("????????????")),
                createResponseFieldsSnippet()
            )
        );

  }


  @Test
  void testPostPart() throws Exception {
    final String apiPath = API_PATH + "/post/part";
    final HttpMethod httpMethod = HttpMethod.POST;
    final String docPath = super.getDocPath(httpMethod, apiPath);

    // ?????? ??????
    ReqDto reqDto = new ReqDto();
    reqDto.setId(1);

    byte[] fileBytes = Files.readAllBytes(Path.of("./src/test/resources/test.png"));

    MockMultipartHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders.multipart(apiPath);
    requestBuilder.file(new MockMultipartFile("reqDto", null, MediaType.APPLICATION_JSON_VALUE, JsonUtil.write(reqDto).getBytes(StandardCharsets.UTF_8)))
        .file(new MockMultipartFile("files", "test1.png", MediaType.IMAGE_PNG_VALUE, fileBytes))
        .file(new MockMultipartFile("files", "test2.png", MediaType.IMAGE_PNG_VALUE, fileBytes))
        .contentType(MediaType.MULTIPART_FORM_DATA);

    // ??????
    ResultActions resultActions = this.mockMvc.perform(requestBuilder);

    // ?????? ?????? ??? ?????????
    resultActions.andDo(print())
        .andExpect(status().isOk())
        .andDo(
            document(docPath,
                super.getDocumentRequest(),
                super.getDocumentResponse(),
                requestParts(
                    partWithName("reqDto").description("JSON ??????"),
                    partWithName("files").description("????????????")
                ),
                requestPartFields("reqDto", List.of(fieldWithPath("id").description("????????????")))
            )
        );

  }

  private ResponseFieldsSnippet createResponseFieldsSnippet() {
    return responseFields(fieldWithPath("id").type(JsonFieldType.NUMBER).description("id"),
        fieldWithPath("name").type(JsonFieldType.STRING).description("??????"),
        fieldWithPath("currentAt").type(JsonFieldType.STRING).optional().description("????????????"));
  }
}
