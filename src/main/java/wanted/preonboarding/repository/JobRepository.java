package wanted.preonboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.preonboarding.domain.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
}
