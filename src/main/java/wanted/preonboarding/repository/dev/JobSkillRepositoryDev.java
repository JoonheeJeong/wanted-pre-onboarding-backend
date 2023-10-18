package wanted.preonboarding.repository.dev;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import wanted.preonboarding.domain.Job;
import wanted.preonboarding.domain.JobSkill;

import java.util.List;

@Profile("dev")
public interface JobSkillRepositoryDev extends JpaRepository<JobSkill, Long> {

    List<JobSkill> findByJob(Job job);
}
