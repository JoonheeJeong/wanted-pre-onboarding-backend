package wanted.preonboarding.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wanted.preonboarding.domain.Skill;
import wanted.preonboarding.domain.type.SkillName;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SkillRepositoryTest {

    @Autowired
    private SkillRepository sut;

    @DisplayName("이름으로 조회")
    @Nested
    class FindByName {

        @DisplayName("성공")
        @Test
        void pass() {
            // given
            SkillName[] names = SkillName.values();
            for (int i = 0; i < names.length; ++i) {
                // when
                Optional<Skill> skill = sut.findByName(names[i]);
                // then
                assertThat(skill).isPresent();
            }
        }
    }

}