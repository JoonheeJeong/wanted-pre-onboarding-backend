package wanted.preonboarding.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import java.util.Set;

import static wanted.preonboarding.domain.type.JobPosition.*;

@AllArgsConstructor
public enum JobGroup {

    MANAGEMENT("경영", Set.of(PL, PM)),
    DESIGN("디자인", Set.of(UX, UI)),
    DEVELOPMENT("개발", Set.of(FE, BE)),
    MARKETING("마케팅", Set.of(CONTENT_MARKETER, BRAND_MARKETER)),
    SALES("영업", Set.of(SALES_MANAGER, SALESMAN));

    private final String description;
    private final Set<JobPosition> positionSet;

    @JsonValue
    public String getDescription() {
        return description;
    }
}
