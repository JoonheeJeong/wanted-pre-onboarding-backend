package wanted.preonboarding.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.preonboarding.dto.JobDetailDTO;
import wanted.preonboarding.dto.JobInfoDTO;
import wanted.preonboarding.dto.JobRegisterDTO;
import wanted.preonboarding.dto.JobUpdateDTO;
import wanted.preonboarding.global.response.ApiMessageResponse;
import wanted.preonboarding.service.JobService;

import java.util.List;

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

    @PutMapping("/{jobId}")
    public ResponseEntity<?> update(
            @PathVariable Long jobId,
            @Valid @RequestBody JobUpdateDTO.Request dto) {

        jobService.update(jobId, dto);
        return ApiMessageResponse.of(HttpStatus.OK, "채용공고가 수정되었습니다.");
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<?> update(@PathVariable Long jobId) {
        jobService.delete(jobId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> getList() {
        List<JobInfoDTO> body = jobService.getList();
        return makeOkResponseWithOnlyData(body);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<?> getDetail(@PathVariable Long jobId) {
        JobDetailDTO body = jobService.getDetail(jobId);
        return makeOkResponseWithOnlyData(body);
    }

    private static ResponseEntity<?> makeOkResponseWithOnlyData(Object body) {
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .body(body);
    }
}
