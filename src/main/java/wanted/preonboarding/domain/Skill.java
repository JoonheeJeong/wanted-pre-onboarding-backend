package wanted.preonboarding.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import wanted.preonboarding.domain.base.AutoIdEntity;
import wanted.preonboarding.domain.type.SkillName;

import java.util.List;

@Entity
public class Skill extends AutoIdEntity {

    @Enumerated(EnumType.STRING)
    @Column(insertable = false, updatable = false, nullable = false, unique = true, length = 30)
    private SkillName name;
}
