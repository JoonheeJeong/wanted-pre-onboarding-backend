package wanted.preonboarding.global.exception;

public class NotFoundJobException extends IllegalArgumentException {

    private static final String DEFAULT_MESSAGE = "해당 ID의 채용공고는 존재하지 않습니다.";

    public NotFoundJobException() {
        this(DEFAULT_MESSAGE);
    }

    public NotFoundJobException(String s) {
        super(s);
    }
}
