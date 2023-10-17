package wanted.preonboarding.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import static wanted.preonboarding.domain.type.JobGroup.*;

@AllArgsConstructor
public enum JobPosition {

    PL("프로젝트 리더", MANAGEMENT),
    PM("제품 매니저", MANAGEMENT),
    UX("UX 디자이너", DESIGN),
    UI("UI 디자이너", DESIGN),
    FE("프론트엔드 개발자", DEVELOPMENT),
    BE("백엔드 개발자", DEVELOPMENT),
    CONTENT_MARKETER("컨텐츠 마케터", MARKETING),
    BRAND_MARKETER("브랜드 마케터", MARKETING),
    SALES_MANAGER("영업 관리자", SALES),
    SALESMAN("영업 사원", SALES);

    private final String description;
    private final JobGroup group;

    @JsonValue
    public String getDescription() {
        return description;
    }
}
