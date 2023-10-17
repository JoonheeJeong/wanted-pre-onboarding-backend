package wanted.preonboarding.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SkillName {

    JAVA("Java"),
    SPRING_FRAMEWORK("Spring Framework"),
    JPA("JPA"),
    SQL("SQL"),
    JAVASCRIPT("JavaScript"),
    REACT("React"),
    GIT("Git"),
    AWS("AWS"),
    DOCKER("Docker");

    private final String description;

    @JsonValue
    public String getDescription() {
        return description;
    }
}
