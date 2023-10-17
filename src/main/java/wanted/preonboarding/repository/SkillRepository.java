package wanted.preonboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.preonboarding.domain.Skill;
import wanted.preonboarding.domain.type.SkillName;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findByName(SkillName name);
}
