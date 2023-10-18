package wanted.preonboarding.domain;

import jakarta.persistence.*;
import lombok.*;
import wanted.preonboarding.domain.base.AutoIdAndCreatedAtAndUpdatedAtEntity;
import wanted.preonboarding.domain.type.Career;
import wanted.preonboarding.domain.type.JobGroup;
import wanted.preonboarding.domain.type.JobPosition;
import wanted.preonboarding.dto.JobRegisterDTO;

import java.util.ArrayList;
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
    @OneToMany(mappedBy = "job")
    private List<JobSkill> jobSkills = new ArrayList<>();

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
}
