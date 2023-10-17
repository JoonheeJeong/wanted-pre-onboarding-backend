package wanted.preonboarding.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import wanted.preonboarding.domain.base.AutoIdEntity;
import wanted.preonboarding.domain.type.SkillName;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class JobSkill extends AutoIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    public static List<JobSkill> from(Job savedJob, List<Skill> skills) {
        return skills.stream()
                .map(skill -> new JobSkill(savedJob, skill))
                .collect(Collectors.toList());
    }
}
