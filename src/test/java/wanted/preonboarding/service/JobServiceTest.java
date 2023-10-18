package wanted.preonboarding.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.domain.Job;
import wanted.preonboarding.domain.JobSkill;
import wanted.preonboarding.domain.Skill;
import wanted.preonboarding.domain.type.Career;
import wanted.preonboarding.domain.type.JobGroup;
import wanted.preonboarding.domain.type.JobPosition;
import wanted.preonboarding.domain.type.SkillName;
import wanted.preonboarding.dto.JobRegisterDTO;
import wanted.preonboarding.dto.JobUpdateDTO;
import wanted.preonboarding.global.exception.NotFoundJobException;
import wanted.preonboarding.repository.JobRepository;
import wanted.preonboarding.repository.dev.JobSkillRepositoryDev;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
            Long companyId = 1L;
            List<SkillName> registerSkills = List.of(SkillName.JAVA, SkillName.SPRING_FRAMEWORK, SkillName.JPA, SkillName.AWS);
            JobRegisterDTO.Request registerRequestDto = makeRegisterDto(companyId, registerSkills);

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

    @DisplayName("채용공고 수정 서비스")
    @Nested
    class Update {

        @DisplayName("[성공] 채용기술 수정")
        @Test
        void givenDifferentSkills_thenUpdateThem() {
            // given
            Long companyId = 1L;
            List<SkillName> registerSkills = List.of(SkillName.JAVA, SkillName.SPRING_FRAMEWORK, SkillName.JPA, SkillName.AWS);
            JobRegisterDTO.Request registerRequestDto = makeRegisterDto(companyId, registerSkills);
            Job savedJob = sut.register(registerRequestDto);

            List<SkillName> updateSkills = List.of(SkillName.JAVA, SkillName.JPA, SkillName.SPRING_FRAMEWORK, SkillName.SQL);
            JobUpdateDTO.Request updateRequestDto = JobUpdateDTO.Request.builder()
                    .group(JobGroup.DEVELOPMENT)
                    .position(JobPosition.BE)
                    .career(Career.SENIOR)
                    .reward(500000)
                    .content("원티드랩에서 백엔드 시니어 개발자를 '적극' 채용합니다. 자격요건은..")
                    .skills(updateSkills)
                    .build();

            // when
            Job updatedJob = sut.update(savedJob.getId(), updateRequestDto);

            // then
            assertThat(updatedJob.getReward()).isEqualTo(updateRequestDto.getReward());
            assertThat(updatedJob.getContent()).isEqualTo(updateRequestDto.getContent());

            List<JobSkill> foundJobSkills = validationRepository.findByJob(updatedJob);
            List<Skill> foundSkills = convertToSkills(foundJobSkills);
            List<Skill> skills = convertToSkills(updatedJob.getJobSkills());
            assertThat(foundSkills).isEqualTo(skills);
        }

        private List<Skill> convertToSkills(List<JobSkill> jobSkills) {
            return jobSkills.stream()
                    .map(JobSkill::getSkill)
                    .toList();
        }

    }
    @DisplayName("채용공고 삭제 서비스")
    @Nested
    class Delete {

        @DisplayName("성공")
        @Test
        void givenNonexistentJobId_thenThrowsException() {
            // given
            final Long companyId = 1L;
            List<SkillName> registerSkills = List.of(SkillName.JAVA, SkillName.SPRING_FRAMEWORK, SkillName.JPA, SkillName.AWS);
            JobRegisterDTO.Request registerDto = makeRegisterDto(companyId, registerSkills);
            Job job = sut.register(registerDto);

            // when
            sut.delete(job.getId());

            // then
            Optional<Job> foundJob = jobRepository.findById(job.getId());
            assertThat(foundJob).isEmpty();
        }
    }

    private static JobRegisterDTO.Request makeRegisterDto(Long companyId, List<SkillName> registerSkills) {
        return JobRegisterDTO.Request.builder()
                .companyId(companyId)
                .group(JobGroup.DEVELOPMENT)
                .position(JobPosition.BE)
                .career(Career.SENIOR)
                .reward(300000)
                .content("원티드랩에서 백엔드 시니어 개발자를 채용합니다. 자격요건은..")
                .skills(registerSkills)
                .build();
    }
}