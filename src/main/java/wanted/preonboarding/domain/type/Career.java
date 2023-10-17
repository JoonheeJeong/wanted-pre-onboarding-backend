package wanted.preonboarding.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Career {

    JUNIOR("주니어"),
    MIDDLE("미들"),
    SENIOR("시니어");

    private final String description;

    @JsonValue
    public String getDescription() {
        return description;
    }
}
