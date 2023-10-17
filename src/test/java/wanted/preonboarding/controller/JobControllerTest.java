package wanted.preonboarding.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
            String requestBody = REQUEST_FORM.formatted(
                    1L,
                    "개발",
                    "백엔드 개발자",
                    "주니어",
                    1_000_000,
                    "원티드랩에서 백엔드 주니어 개발자를 '적극' 채용합니다. 자격요건은..",
                    "\"Java\",\"Spring Framework\",\"JPA\",\"SQL\"");

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"채용공고가 등록되었습니다.\"}"))
                    .andReturn();
        }

        @DisplayName("400 유효하지 않은 직군")
        @Test
        void givenInvalidJobGroup_thenRespond400() throws Exception {
            String requestBody = REQUEST_FORM.formatted(
                    1L,
                    "개발1",
                    "백엔드 개발자",
                    "주니어",
                    1_000_000,
                    "원티드랩에서 백엔드 주니어 개발자를 '적극' 채용합니다. 자격요건은..",
                    "\"Java\",\"Spring Framework\",\"JPA\",\"SQL\"");

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"입력이 유효하지 않습니다.\"}"))
                    .andReturn();
        }
    }
}