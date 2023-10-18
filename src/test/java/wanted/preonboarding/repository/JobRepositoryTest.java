package wanted.preonboarding.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wanted.preonboarding.domain.Company;
import wanted.preonboarding.domain.Job;
import wanted.preonboarding.domain.type.Career;
import wanted.preonboarding.domain.type.JobGroup;
import wanted.preonboarding.domain.type.JobPosition;
import wanted.preonboarding.domain.type.SkillName;
import wanted.preonboarding.dto.JobRegisterDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JobRepositoryTest {

    @Autowired
    private JobRepository sut;
    @Autowired
    private CompanyRepository companyRepository;

    @DisplayName("채용공고 저장")
    @Nested
    class Save {

        @DisplayName("성공")
        @Test
        void pass() {
            // given
            Long companyId = 1L;
            JobRegisterDTO.Request dto = JobRegisterDTO.Request.builder()
                    .companyId(companyId)
                    .group(JobGroup.DEVELOPMENT)
                    .position(JobPosition.BE)
                    .career(Career.JUNIOR)
                    .reward(100000)
                    .content("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..")
                    .skills(List.of(SkillName.JAVA, SkillName.SPRING_FRAMEWORK, SkillName.JPA, SkillName.SQL))
                    .build();
            Company company = companyRepository.findById(companyId).orElseThrow();

            Job job = Job.from(dto, company);

            // when
            sut.save(job);

            // then
            assertThat(job.getId()).isNotNull();
        }
    }

}