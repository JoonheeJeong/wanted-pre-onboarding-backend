package wanted.preonboarding.dto;

import lombok.*;
import wanted.preonboarding.domain.*;
import wanted.preonboarding.domain.type.Career;
import wanted.preonboarding.domain.type.JobGroup;
import wanted.preonboarding.domain.type.JobPosition;
import wanted.preonboarding.domain.type.SkillName;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class JobInfoDTO {

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

    public static JobInfoDTO from(Job job) {
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
                .build();
    }
}
