package wanted.preonboarding.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.preonboarding.dto.JobRegisterDTO;
import wanted.preonboarding.global.response.ApiMessageResponse;
import wanted.preonboarding.service.JobService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/jobs")
@RestController
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody JobRegisterDTO.Request dto) {
        jobService.register(dto);
        return ApiMessageResponse.of(HttpStatus.CREATED, "채용공고가 등록되었습니다.");
    }
}
