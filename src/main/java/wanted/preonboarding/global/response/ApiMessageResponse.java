package wanted.preonboarding.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
public class ApiMessageResponse {

    private final String message;

    public static ResponseEntity<?> of(HttpStatus httpStatus, String message) {
        ApiMessageResponse response = new ApiMessageResponse(message);
        return ResponseEntity.status(httpStatus)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .body(response);
    }
}
