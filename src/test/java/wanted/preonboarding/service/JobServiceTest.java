package wanted.preonboarding.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.domain.Job;
import wanted.preonboarding.domain.JobSkill;
import wanted.preonboarding.domain.type.Career;
import wanted.preonboarding.domain.type.JobGroup;
import wanted.preonboarding.domain.type.JobPosition;
import wanted.preonboarding.domain.type.SkillName;
import wanted.preonboarding.dto.JobRegisterDTO;
import wanted.preonboarding.repository.JobRepository;
import wanted.preonboarding.repository.dev.JobSkillRepositoryDev;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class JobServiceTest {

    @Autowired
    private JobService sut;
    @Autowired
    private JobSkillRepositoryDev validationRepository;
    @Autowired
    private JobRepository jobRepository;

    @DisplayName("채용공고 등록 서비스")
    @Nested
    class Register {

        @DisplayName("성공")
        @Test
        void pass() {
            // given
            List<SkillName> registerSkills = List.of(SkillName.JAVA, SkillName.SPRING_FRAMEWORK, SkillName.JPA, SkillName.AWS);
            Long companyId = 1L;
            JobRegisterDTO.Request registerRequestDto = JobRegisterDTO.Request.builder()
                    .companyId(companyId)
                    .group(JobGroup.DEVELOPMENT)
                    .position(JobPosition.BE)
                    .career(Career.SENIOR)
                    .reward(300000)
                    .content("원티드랩에서 백엔드 시니어 개발자를 채용합니다. 자격요건은..")
                    .skills(registerSkills)
                    .build();

            // when
            Job savedJob = sut.register(registerRequestDto);

            // then
            List<JobSkill> foundJobSkills = validationRepository.findByJob(savedJob);
            assertThat(foundJobSkills).hasSize(registerSkills.size());
            assertThat(savedJob.getJobSkills()).hasSize(registerSkills.size());

            Job foundJob = jobRepository.findById(savedJob.getId()).orElseThrow();
            assertThat(foundJob).isSameAs(savedJob);
        }
    }
}