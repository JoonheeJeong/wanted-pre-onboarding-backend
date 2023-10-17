package wanted.preonboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.preonboarding.domain.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
