package wanted.preonboarding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.domain.Company;
import wanted.preonboarding.domain.Job;
import wanted.preonboarding.domain.JobSkill;
import wanted.preonboarding.domain.Skill;
import wanted.preonboarding.domain.type.SkillName;
import wanted.preonboarding.dto.JobDetailDTO;
import wanted.preonboarding.dto.JobInfoDTO;
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

        addJobSkills(dto.getSkills(), job);

        return jobRepository.save(job);
    }

    private void addJobSkills(List<SkillName> skillNames, Job job) {
        skillNames.stream()
                .map(skillName -> {
                    Skill skill = getSkill(skillName);
                    return JobSkill.from(job, skill);
                })
                .forEach(job::addJobSkill);
    }

    @Transactional
    public Job update(Long jobId, JobUpdateDTO.Request dto) {
        Job job = getJob(jobId);

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
                .map(this::getSkill)
                .toList();
    }

    private Skill getSkill(SkillName skillName) {
        return skillRepository.findByName(skillName)
                .orElseThrow(NotFoundSkillByNameException::new);
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
        Job job = getJob(jobId);
        jobRepository.delete(job);
    }

    @Transactional(readOnly = true)
    public List<JobInfoDTO> getList() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream()
                .map(JobInfoDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public JobDetailDTO getDetail(Long jobId) {
        Job job = getJob(jobId);
        List<Long> jobIds = jobRepository.findOtherIds(jobId, job.getCompany());
        return JobDetailDTO.from(job, jobIds);
    }

    private Job getJob(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(NotFoundJobException::new);
    }
}
