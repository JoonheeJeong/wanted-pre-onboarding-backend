package wanted.preonboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.preonboarding.domain.JobSkill;

public interface JobSkillRepository extends JpaRepository<JobSkill, Long> {
}
