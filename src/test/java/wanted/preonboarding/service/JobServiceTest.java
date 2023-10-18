package wanted.preonboarding.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wanted.preonboarding.domain.Company;
import wanted.preonboarding.domain.Job;
import wanted.preonboarding.domain.Skill;
import wanted.preonboarding.domain.type.SkillName;
import wanted.preonboarding.dto.JobRegisterDTO;
import wanted.preonboarding.repository.CompanyRepository;
import wanted.preonboarding.repository.JobRepository;
import wanted.preonboarding.repository.JobSkillRepository;
import wanted.preonboarding.repository.SkillRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @InjectMocks
    private JobService sut;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobSkillRepository jobSkillRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private SkillRepository skillRepository;

    @DisplayName("채용등록")
    @Nested
    class Register {

        @DisplayName("성공")
        @Test
        void pass() {
            // given
            List<SkillName> skills = List.of(SkillName.JAVA, SkillName.SPRING_FRAMEWORK, SkillName.JPA, SkillName.SQL);
            JobRegisterDTO.Request dto = mock(JobRegisterDTO.Request.class);
            given(dto.getCompanyId()).willReturn(1L);
            given(dto.getSkills()).willReturn(skills);

            Company company = mock(Company.class);
            given(companyRepository.findById(anyLong()))
                    .willReturn(Optional.of(company));

            Skill skill = mock(Skill.class);
            skills.forEach(skillName ->
                    given(skillRepository.findByName(skillName))
                            .willReturn(Optional.of(skill)));

            // when
            sut.register(dto);

            // then
            verify(companyRepository, times(1))
                    .findById(eq(dto.getCompanyId()));
            verify(jobRepository, times(1))
                    .save(any(Job.class));
            verify(skillRepository, times(skills.size()))
                    .findByName(any(SkillName.class));
            verify(jobSkillRepository, times(1))
                    .saveAll(any());
        }
    }
}