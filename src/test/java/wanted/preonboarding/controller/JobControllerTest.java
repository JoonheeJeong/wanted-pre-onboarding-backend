package wanted.preonboarding.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class JobControllerTest {

    private static final String BASE_URI = "/api/v1/job";
    private static final String REQUEST_FORM = """
            {
                "companyId": %d,
                "group": "%s",
                "position": "%s",
                "career": "%s",
                "reward": %d,
                "content": "%s",
                "skills": [ %s ]
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("채용공고 등록")
    class RegisterTest {

        @DisplayName("201 성공")
        @Test
        void pass() throws Exception {
            String requestBody = getRequestBody("개발");

            performMvc(requestBody, status().isCreated(), "{\"message\":\"채용공고가 등록되었습니다.\"}");
        }

        @DisplayName("400 유효하지 않은 직군")
        @Test
        void givenInvalidJobGroup_thenRespond400() throws Exception {
            String requestBody = getRequestBody("개발1");

            performMvc(requestBody, status().isBadRequest(), "{\"message\":\"입력이 유효하지 않습니다.\"}");
        }

        private void performMvc(String requestBody, ResultMatcher resultMatcher, String jsonContent) throws Exception {
            mockMvc.perform(post(BASE_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(resultMatcher)
                    .andExpect(content().json(jsonContent))
                    .andReturn();
        }

        private String getRequestBody(String group) {
            return REQUEST_FORM.formatted(
                    1L,
                    group,
                    "백엔드 개발자",
                    "주니어",
                    1_000_000,
                    "원티드랩에서 백엔드 주니어 개발자를 '적극' 채용합니다. 자격요건은..",
                    "\"Java\",\"Spring Framework\",\"JPA\",\"SQL\"");
        }
    }
}