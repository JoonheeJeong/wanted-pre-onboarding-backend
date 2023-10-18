package wanted.preonboarding.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import wanted.preonboarding.domain.Company;
import wanted.preonboarding.domain.Job;
import wanted.preonboarding.domain.JobSkill;
import wanted.preonboarding.domain.Skill;
import wanted.preonboarding.domain.type.Career;
import wanted.preonboarding.domain.type.JobGroup;
import wanted.preonboarding.domain.type.JobPosition;
import wanted.preonboarding.domain.type.SkillName;
import wanted.preonboarding.dto.JobRegisterDTO;
import wanted.preonboarding.repository.dev.JobSkillRepositoryDev;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JobSkillRepositoryTest {

    @Autowired
    private JobSkillRepository sut;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private JobSkillRepositoryDev validationRepository;

    @DisplayName("채용공고 기술 저장")
    @Nested
    class SaveAll {

        @DisplayName("성공")
        @Test
        void pass() {
            // given
            List<SkillName> skillNames = List.of(SkillName.JAVA, SkillName.SPRING_FRAMEWORK, SkillName.JPA, SkillName.SQL);
            Long companyId = 1L;
            JobRegisterDTO.Request dto = JobRegisterDTO.Request.builder()
                    .companyId(companyId)
                    .group(JobGroup.DEVELOPMENT)
                    .position(JobPosition.BE)
                    .career(Career.JUNIOR)
                    .reward(100000)
                    .content("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..")
                    .skills(skillNames)
                    .build();
            Company company = companyRepository.findById(companyId).orElseThrow();

            Job job = Job.from(dto, company);
            job = jobRepository.save(job);

            List<Skill> skills = skillNames.stream()
                    .map(name -> skillRepository.findByName(name).orElseThrow())
                    .toList();

            // when
            List<JobSkill> jobSkills = JobSkill.from(job, skills);
            sut.saveAll(jobSkills);

            // then
            List<JobSkill> savedJobSkills = validationRepository.findByJob(job);
            assertThat(savedJobSkills).isEqualTo(jobSkills);
        }
    }

}