package wanted.preonboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wanted.preonboarding.domain.Company;
import wanted.preonboarding.domain.Job;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("select j.id from Job j where j.company = :company and j.id != :jobId")
    List<Long> findOtherIds(Long jobId, Company company);
}
