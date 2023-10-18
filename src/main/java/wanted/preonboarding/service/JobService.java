package wanted.preonboarding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.domain.Company;
import wanted.preonboarding.domain.Job;
import wanted.preonboarding.domain.JobSkill;
import wanted.preonboarding.domain.Skill;
import wanted.preonboarding.dto.JobRegisterDTO;
import wanted.preonboarding.global.exception.NotFoundCompanyException;
import wanted.preonboarding.global.exception.NotFoundSkillByNameException;
import wanted.preonboarding.repository.CompanyRepository;
import wanted.preonboarding.repository.JobRepository;
import wanted.preonboarding.repository.SkillRepository;

@RequiredArgsConstructor
@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final SkillRepository skillRepository;

    @Transactional
    public void register(JobRegisterDTO.Request dto) {
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(NotFoundCompanyException::new);

        final Job job = Job.from(dto, company);

        dto.getSkills().stream()
                .map(skillName -> {
                    Skill skill = skillRepository.findByName(skillName)
                            .orElseThrow(NotFoundSkillByNameException::new);
                    return JobSkill.from(job, skill);
                })
                .forEach(job::addJobSkill);

        jobRepository.save(job);
    }
}
