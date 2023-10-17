package wanted.preonboarding.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import wanted.preonboarding.domain.type.Career;
import wanted.preonboarding.domain.type.JobGroup;
import wanted.preonboarding.domain.type.JobPosition;
import wanted.preonboarding.domain.type.SkillName;

import java.util.List;

public class JobRegisterDTO {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Getter
    public static class Request {

        @NotNull
        private Long companyId;
        @NotNull
        private JobGroup group;
        @NotNull
        private JobPosition position;
        @NotNull
        private Career career;
        @NotNull
        @Min(100_000)
        private Integer reward;
        @NotEmpty
        private String content;
        @NotEmpty
        private List<SkillName> skills;
    }
}
