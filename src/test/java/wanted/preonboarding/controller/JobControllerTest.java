package wanted.preonboarding.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import wanted.preonboarding.domain.type.Career;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
class JobControllerTest {

    private static final String BASE_URI = "/api/v1/jobs";

    @Autowired
    private MockMvc mockMvc;

    @Order(1)
    @Nested
    @DisplayName("채용공고 등록")
    class RegisterTest {

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

        @DisplayName("201 성공")
        @Test
        void pass() throws Exception {
            List<String> careers = Arrays.stream(Career.values())
                    .map(Career::getDescription)
                    .toList();
            for (String career : careers) {
                String requestBody = getRequestBody(career);
                performMvc(requestBody, status().isCreated(), "{\"message\":\"채용공고가 등록되었습니다.\"}");
            }
        }

        @DisplayName("400 유효하지 않은 커리어")
        @Test
        void givenInvalidJobGroup_thenRespond400() throws Exception {
            String requestBody = getRequestBody("주니어1");

            performMvc(requestBody, status().isBadRequest(), "{\"message\":\"입력이 유효하지 않습니다.\"}");
        }

        private static String getRequestBody(String career) {
            return REQUEST_FORM.formatted(
                    1L,
                    "개발",
                    "백엔드 개발자",
                    career,
                    1_000_000,
                    "원티드랩에서 백엔드 개발자를 채용합니다. 자격요건은..",
                    "\"Java\",\"Spring Framework\",\"JPA\",\"SQL\"");
        }

        private void performMvc(String requestBody, ResultMatcher resultMatcher, String jsonContent) throws Exception {
            mockMvc.perform(post(BASE_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(resultMatcher)
                    .andExpect(content().json(jsonContent))
                    .andReturn();
        }
    }

    @Order(2)
    @DisplayName("채용공고 수정")
    @Nested
    class Update {

        private static final String REQUEST_FORM = """
                {
                    "group": "%s",
                    "position": "%s",
                    "career": "%s",
                    "reward": %d,
                    "content": "%s",
                    "skills": [ %s ]
                }
                """;

        @DisplayName("400 유효하지 않은 직군")
        @Test
        void givenInvalidJobGroup_thenRespond400() throws Exception {
            String requestBody = getRequestBody("개발1");

            performMvc(requestBody, status().isBadRequest(), "{\"message\":\"입력이 유효하지 않습니다.\"}");
        }

        @DisplayName("200 보상금,내용 수정 성공")
        @Test
        void pass() throws Exception {
            String requestBody = getRequestBody("개발");

            performMvc(requestBody, status().isOk(), "{\"message\":\"채용공고가 수정되었습니다.\"}");
        }

        @DisplayName("200 기술 수정 성공")
        @Test
        void givenDifferentSkills_thenOk() throws Exception {
            String requestBody = getRequestBody("개발", "\"AWS\",\"Java\",\"SQL\",\"Spring Framework\"");

            performMvc(requestBody, status().isOk(), "{\"message\":\"채용공고가 수정되었습니다.\"}");
        }

        private static String getRequestBody(String group) {
            return getRequestBody(group, "\"Java\",\"Spring Framework\",\"JPA\",\"SQL\"");
        }

        private static String getRequestBody(String group, String skills) {
            return REQUEST_FORM.formatted(
                    group,
                    "백엔드 개발자",
                    "주니어",
                    2_000_000,
                    "원티드랩에서 백엔드 주니어 개발자를 '적극' 채용합니다. 자격요건은..",
                    skills);
        }

        private void performMvc(String requestBody, ResultMatcher resultMatcher, String jsonContent) throws Exception {
            long jobId = 1L;
            mockMvc.perform(put(BASE_URI + "/" + jobId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(resultMatcher)
                    .andExpect(content().json(jsonContent))
                    .andReturn();
        }
    }

    @Order(10000)
    @DisplayName("채용공고 삭제")
    @Nested
    class Delete {

        @DisplayName("400 채용공고가 존재하지 않음")
        @Test
        void givenNonexistentJobId_thenRespond400() throws Exception {
            // given
            final long jobId = 0L;

            // when, then
            mockMvc.perform(delete(BASE_URI + "/" + jobId))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("{\"message\":\"해당 ID의 채용공고는 존재하지 않습니다.\"}"))
                    .andReturn();
        }
    }

    @Order(3)
    @DisplayName("채용공고 목록 조회")
    @Nested
    class GetList {

        @DisplayName("200 성공")
        @Test
        void pass() throws Exception {
            mockMvc.perform(get(BASE_URI)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andReturn();
        }
    }


    @Order(4)
    @DisplayName("채용공고 상세 조회")
    @Nested
    class GetDetail {

        @DisplayName("400 채용공고가 존재하지 않음")
        @Test
        void givenNonexistentJobId_thenRespond400() throws Exception {
            final long jobId = 0L;

            mockMvc.perform(get(BASE_URI + "/" + jobId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("{\"message\":\"해당 ID의 채용공고는 존재하지 않습니다.\"}"))
                    .andDo(print())
                    .andReturn();
        }

        @DisplayName("200 성공")
        @Test
        void pass() throws Exception {
            final long jobId = 1L;

            MvcResult mvcResult = mockMvc.perform(get(BASE_URI + "/" + jobId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andReturn();

            String content = mvcResult.getResponse().getContentAsString();
            org.assertj.core.api.Assertions.assertThat(content).contains("\"jobId\":" + jobId);
            Assertions.assertThat(content).contains("\"otherJobIds\":[2,3]");
        }
    }
}
