package wanted.preonboarding.global.exception;

public class NotFoundSkillByNameException extends IllegalArgumentException {

    private static final String DEFAULT_MESSAGE = "해당 이름의 기술은 존재하지 않습니다.";

    public NotFoundSkillByNameException() {
        this(DEFAULT_MESSAGE);
    }

    public NotFoundSkillByNameException(String s) {
        super(s);
    }
}
