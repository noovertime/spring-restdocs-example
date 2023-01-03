package com.noovertime.ex.restdocs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import jakarta.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@Slf4j
public abstract class AbstractTest {

  private final ManualRestDocumentation restDocumentation = new ManualRestDocumentation();

  protected MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;


  @BeforeEach
  protected void beforeEach(TestInfo testInfo) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
        .apply(documentationConfiguration(this.restDocumentation))
        // 없으면 응답에 한글 깨짐
        .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
        .build();

    String methodName = "__UNKNOWN__";

    if (testInfo.getTestMethod().isPresent()) {
      methodName = testInfo.getTestMethod().get().getName();
    }

    this.restDocumentation.beforeTest(testInfo.getClass(), methodName);
  }

  @AfterEach
  public void afterEach() {
    this.restDocumentation.afterTest();
  }


  protected String getDocPath(@NotNull HttpMethod method, @NotNull String apiPath) {
    return apiPath.toLowerCase().replaceAll("/", "_"); // + method.name().toLowerCase();
  }


  /**
   * 요청문서 옵션
   *
   * @return 옵션들
   */
  protected OperationRequestPreprocessor getDocumentRequest() {
    return preprocessRequest(prettyPrint(),
        modifyUris().host("localhost").port(80),
        modifyHeaders().remove("User-Agent"));


  }

  /**
   * 응답문서 옵션
   *
   * @return 옵션들
   */
  protected OperationResponsePreprocessor getDocumentResponse() {
    return preprocessResponse(prettyPrint(),
        modifyHeaders().remove("")
    );
  }


}
