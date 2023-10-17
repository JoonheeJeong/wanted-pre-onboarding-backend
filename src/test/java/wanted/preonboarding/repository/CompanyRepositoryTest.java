package wanted.preonboarding.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wanted.preonboarding.domain.Company;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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

        @DisplayName("존재하지 않음")
        @Test
        void givenNotExistent_thenReturnNull() {
            // given
            Long id = (long) SIZE + 1;

            // when
            Optional<Company> company = sut.findById(id);

            // then
            assertThat(company).isEmpty();
        }
    }
}