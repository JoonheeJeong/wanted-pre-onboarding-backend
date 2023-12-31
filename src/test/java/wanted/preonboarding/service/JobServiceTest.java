package wanted.preonboarding.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.domain.Company;
import wanted.preonboarding.domain.Job;
import wanted.preonboarding.domain.JobSkill;
import wanted.preonboarding.domain.Skill;
import wanted.preonboarding.domain.type.Career;
import wanted.preonboarding.domain.type.JobGroup;
import wanted.preonboarding.domain.type.JobPosition;
import wanted.preonboarding.domain.type.SkillName;
import wanted.preonboarding.dto.JobDetailDTO;
import wanted.preonboarding.dto.JobInfoDTO;
import wanted.preonboarding.dto.JobRegisterDTO;
import wanted.preonboarding.dto.JobUpdateDTO;
import wanted.preonboarding.repository.JobRepository;
import wanted.preonboarding.repository.dev.JobSkillRepositoryDev;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static wanted.preonboarding.domain.type.SkillName.*;

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
            List<SkillName> registerSkills = List.of(JAVA, SPRING_FRAMEWORK, JPA, AWS);
            JobRegisterDTO.Request registerRequestDto = makeRegisterDto(COMPANY_ID, CAREER, registerSkills);

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
            JobRegisterDTO.Request registerRequestDto = makeRegisterDto();
            Job savedJob = sut.register(registerRequestDto);

            List<SkillName> updateSkills = List.of(JAVA, JPA, SPRING_FRAMEWORK, SQL);
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
            JobRegisterDTO.Request registerDto = makeRegisterDto();
            Job job = sut.register(registerDto);

            // when
            sut.delete(job.getId());

            // then
            Optional<Job> foundJob = jobRepository.findById(job.getId());
            assertThat(foundJob).isEmpty();
        }
    }

    @DisplayName("채용공고 목록 조회 서비스")
    @Nested
    class GetList {

        @DisplayName("성공")
        @Test
        void pass() {
            // given
            final int size = 3;
            List<Job> jobs = new ArrayList<>();
            for (int companyId = 1; companyId <= size; companyId++) {
                JobRegisterDTO.Request registerDto = makeRegisterDto(companyId, CAREER, SKILL_NAMES);
                Job job = sut.register(registerDto);
                jobs.add(job);
            }

            // when
            List<JobInfoDTO> list = sut.getList();

            // then
            assertThat(list).hasSize(size);
            for (int i = 0; i < size; ++i) {
                Job job = jobs.get(i);
                Company company = job.getCompany();
                JobInfoDTO dto = list.get(i);
                assertThat(dto.getCompanyName()).isEqualTo(company.getName());
                assertThat(dto.getCity()).isEqualTo(company.getRegion().getCity());
                assertThat(dto.getGroup()).isSameAs(job.getJobGroup());
                assertThat(dto.getReward()).isEqualTo(job.getReward());
                List<SkillName> skillNames = job.getJobSkills().stream()
                        .map(JobSkill::getSkill)
                        .map(Skill::getName)
                        .toList();
                assertThat(dto.getSkills()).isEqualTo(skillNames);
            }
        }
    }

    @DisplayName("채용공고 상세 조회 서비스")
    @Nested
    class GetDetail {

        @DisplayName("성공")
        @Test
        void pass() {
            // given
            Career[] values = Career.values();
            final int size = values.length;
            List<Job> jobs = new ArrayList<>();
            for (Career value : values) {
                JobRegisterDTO.Request registerDto = makeRegisterDto(COMPANY_ID, value, SKILL_NAMES);
                Job job = sut.register(registerDto);
                jobs.add(job);
            }
            Job job = jobs.get(0);
            final long jobId = job.getId();

            // when
            JobDetailDTO dto = sut.getDetail(jobId);

            // then
            Company company = job.getCompany();
            List<Long> otherJobIds = dto.getOtherJobIds();
            assertThat(otherJobIds).hasSize(size - 1);
            for (int i = 1; i < size; ++i) {
                Long anotherJobId = otherJobIds.get(i - 1);
                Job anotherJob = jobs.get(i);
                assertThat(anotherJobId).isEqualTo(anotherJob.getId());
                Job storedAnotherJob = jobRepository.findById(anotherJobId).orElseThrow();
                assertThat(storedAnotherJob).isSameAs(anotherJob);

                assertThat(dto.getCompanyName()).isEqualTo(company.getName());
                assertThat(dto.getCity()).isEqualTo(company.getRegion().getCity());
                assertThat(dto.getGroup()).isSameAs(job.getJobGroup());
                assertThat(dto.getReward()).isEqualTo(job.getReward());
                List<SkillName> skillNames = job.getJobSkills().stream()
                        .map(JobSkill::getSkill)
                        .map(Skill::getName)
                        .toList();
                assertThat(dto.getSkills()).isEqualTo(skillNames);
            }
        }
    }

    private static final long COMPANY_ID = 1L;
    private static final Career CAREER = Career.SENIOR;
    private static final List<SkillName> SKILL_NAMES = List.of(JAVA, SPRING_FRAMEWORK, JPA, AWS);

    private static JobRegisterDTO.Request makeRegisterDto() {
        return makeRegisterDto(COMPANY_ID, CAREER, SKILL_NAMES);
    }

    private static JobRegisterDTO.Request makeRegisterDto(
            long companyId,
            Career career,
            List<SkillName> registerSkills) {
        return JobRegisterDTO.Request.builder()
                .companyId(companyId)
                .group(JobGroup.DEVELOPMENT)
                .position(JobPosition.BE)
                .career(career)
                .reward(300000)
                .content("채용내용...")
                .skills(registerSkills)
                .build();
    }
}
