package wanted.preonboarding.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import wanted.preonboarding.domain.*;
import wanted.preonboarding.domain.type.Career;
import wanted.preonboarding.domain.type.JobGroup;
import wanted.preonboarding.domain.type.JobPosition;
import wanted.preonboarding.domain.type.SkillName;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class JobDetailDTO {

    private Long jobId;
    private String companyName;
    private String country;
    private String city;
    private JobGroup group;
    private JobPosition position;
    private Career career;
    private Integer reward;
    private String content;
    private List<SkillName> skills;
    private List<Long> otherJobIds;

    public static JobDetailDTO from(Job job, List<Long> jobIds) {
        Company company = job.getCompany();
        Region region = company.getRegion();
        return builder()
                .jobId(job.getId())
                .companyName(company.getName())
                .country(region.getCountry())
                .city(region.getCity())
                .group(job.getJobGroup())
                .position(job.getJobPosition())
                .career(job.getCareer())
                .reward(job.getReward())
                .content(job.getContent())
                .skills(job.getJobSkills().stream()
                        .map(JobSkill::getSkill)
                        .map(Skill::getName)
                        .toList())
                .otherJobIds(jobIds)
                .build();
    }
}
