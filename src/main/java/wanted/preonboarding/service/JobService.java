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
import wanted.preonboarding.repository.JobSkillRepository;
import wanted.preonboarding.repository.SkillRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final JobSkillRepository jobSkillRepository;
    private final SkillRepository skillRepository;

    @Transactional
    public void register(JobRegisterDTO.Request dto) {
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(NotFoundCompanyException::new);

        Job savedJob = jobRepository.save(Job.from(dto, company));

        List<Skill> skills = dto.getSkills().stream()
                .map(skillName -> skillRepository.findByName(skillName)
                        .orElseThrow(NotFoundSkillByNameException::new))
                .collect(Collectors.toList());
        jobSkillRepository.saveAll(JobSkill.from(savedJob, skills));
    }
}
