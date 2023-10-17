package wanted.preonboarding.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wanted.preonboarding.domain.Company;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CompanyRepositoryTest {

    private static final int SIZE = 6;

    @Autowired
    private CompanyRepository sut;

    @DisplayName("PK 조회")
    @Nested
    class FindByName {

        @DisplayName("존재")
        @Test
        void givenExistent_thenReturnIt() {
            for (long i = 1L; i <= SIZE; ++i) {
                Optional<Company> company = sut.findById(i);
                assertThat(company).isPresent();
            }
        }
    }
}