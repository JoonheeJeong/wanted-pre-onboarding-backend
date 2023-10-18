package wanted.preonboarding.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import wanted.preonboarding.domain.base.AutoIdEntity;
import wanted.preonboarding.domain.type.SkillName;

@Getter
@Entity
public class Skill extends AutoIdEntity {

    @Enumerated(EnumType.STRING)
    @Column(insertable = false, updatable = false, nullable = false, unique = true, length = 30)
    private SkillName name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return getId().equals(skill.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
