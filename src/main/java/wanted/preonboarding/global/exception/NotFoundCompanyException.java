package wanted.preonboarding.global.exception;

public class NotFoundCompanyException extends IllegalArgumentException {

    private static final String DEFAULT_MESSAGE = "해당 ID의 회사가 존재하지 않습니다.";

    public NotFoundCompanyException() {
        this(DEFAULT_MESSAGE);
    }

    public NotFoundCompanyException(String s) {
        super(s);
    }
}
