package wanted.preonboarding.domain;

import jakarta.persistence.*;
import lombok.*;
import wanted.preonboarding.domain.base.AutoIdAndCreatedAtAndUpdatedAtEntity;
import wanted.preonboarding.domain.type.Career;
import wanted.preonboarding.domain.type.JobGroup;
import wanted.preonboarding.domain.type.JobPosition;
import wanted.preonboarding.dto.JobRegisterDTO;
import wanted.preonboarding.dto.JobUpdateDTO;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Job extends AutoIdAndCreatedAtAndUpdatedAtEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 40)
    private JobGroup jobGroup;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 40)
    private JobPosition jobPosition;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 40)
    private Career career;
    @Column(nullable = false)
    private Integer reward;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private String content;
    @Builder.Default
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<JobSkill> jobSkills = new LinkedList<>();

    public static Job from(JobRegisterDTO.Request dto, Company company) {
        return Job.builder()
                .company(company)
                .jobGroup(dto.getGroup())
                .jobPosition(dto.getPosition())
                .career(dto.getCareer())
                .content(dto.getContent())
                .reward(dto.getReward())
                .build();
    }

    public void addJobSkill(JobSkill jobSkill) {
        jobSkills.add(jobSkill);
    }

    public void update(JobUpdateDTO.Request dto) {
        updateJobGroup(dto);
        updateJobPosition(dto);
        updateCareer(dto);
        updateReward(dto);
        updateContent(dto);
    }

    private void updateJobGroup(JobUpdateDTO.Request dto) {
        if (jobGroup != dto.getGroup()) {
            jobGroup = dto.getGroup();
        }
    }

    private void updateJobPosition(JobUpdateDTO.Request dto) {
        if (jobPosition != dto.getPosition()) {
            jobPosition = dto.getPosition();
        }
    }

    private void updateCareer(JobUpdateDTO.Request dto) {
        if (career != dto.getCareer()) {
            career = dto.getCareer();
        }
    }

    private void updateReward(JobUpdateDTO.Request dto) {
        if (!reward.equals(dto.getReward())) {
            reward = dto.getReward();
        }
    }

    private void updateContent(JobUpdateDTO.Request dto) {
        if (!content.equals(dto.getContent())) {
            content = dto.getContent();
        }
    }

    public JobSkill removeJobSkill(int index) {
        return jobSkills.remove(index);
    }
}
