package wanted.preonboarding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.domain.Company;
import wanted.preonboarding.domain.Job;
import wanted.preonboarding.domain.JobSkill;
import wanted.preonboarding.domain.Skill;
import wanted.preonboarding.domain.type.SkillName;
import wanted.preonboarding.dto.JobRegisterDTO;
import wanted.preonboarding.dto.JobUpdateDTO;
import wanted.preonboarding.global.exception.NotFoundCompanyException;
import wanted.preonboarding.global.exception.NotFoundJobException;
import wanted.preonboarding.global.exception.NotFoundSkillByNameException;
import wanted.preonboarding.repository.CompanyRepository;
import wanted.preonboarding.repository.JobRepository;
import wanted.preonboarding.repository.JobSkillRepository;
import wanted.preonboarding.repository.SkillRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final SkillRepository skillRepository;
    private final JobSkillRepository jobSkillRepository;

    @Transactional
    public Job register(JobRegisterDTO.Request dto) {
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

        return jobRepository.save(job);
    }

    @Transactional
    public Job update(Long jobId, JobUpdateDTO.Request dto) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(NotFoundJobException::new);

        job.update(dto);

        List<Skill> updatingSkills = convertToSkills(dto.getSkills());

        // 저장되어있고, 수정 데이터에도 존재: 유지

        // 저장되어있는데, 수정 데이터에는 없음: 삭제
        deleteUnusedJobSkills(job, updatingSkills);

        // 저장되어있지 않은데, 수정 데이터에 존재: 추가
        addNewSkills(job, updatingSkills);

        return jobRepository.save(job);
    }

    private List<Skill> convertToSkills(List<SkillName> skillNames) {
        return skillNames.stream()
                .map(skillName -> skillRepository.findByName(skillName)
                        .orElseThrow(NotFoundSkillByNameException::new))
                .toList();
    }

    private void deleteUnusedJobSkills(Job job, List<Skill> updatingSkills) {
        List<JobSkill> jobSkills = job.getJobSkills();
        for (int i = jobSkills.size() - 1; i >= 0; --i) {
            Skill storedSkill = jobSkills.get(i).getSkill();
            if (!updatingSkills.contains(storedSkill)) {
                JobSkill removingJobSkill = job.removeJobSkill(i);
                jobSkillRepository.delete(removingJobSkill);
            }
        }
    }

    private void addNewSkills(Job job, List<Skill> updatingSkills) {
        List<Skill> skills = job.getJobSkills().stream()
                .map(JobSkill::getSkill)
                .toList();
        for (Skill updatingSkill : updatingSkills) {
            if (!skills.contains(updatingSkill)) {
                job.addJobSkill(JobSkill.from(job, updatingSkill));
            }
        }
    }

    @Transactional
    public void delete(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(NotFoundJobException::new);
        jobRepository.delete(job);
    }
}
