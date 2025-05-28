package com.ra.base_spring_boot.controller;
import com.ra.base_spring_boot.dto.MessageResponse;
import com.ra.base_spring_boot.dto.PaginatedResponse;
import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.ExamDto;
import com.ra.base_spring_boot.model.Exam;
import com.ra.base_spring_boot.services.IExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class ExamAdminController {
    private final IExamService examService;

    @PostMapping("/exam")
    public ResponseEntity<ResponseWrapper<Exam>> createExam(@Valid @RequestBody ExamDto req){
        ResponseWrapper<Exam> response = examService.createExam(req);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/exam")
    public ResponseEntity<PaginatedResponse<Exam>> getExams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PaginatedResponse<Exam> response = examService.getExams(page, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/exam/{id}")
    public ResponseEntity<ResponseWrapper<Exam>> updateExam(@PathVariable Long id, @Valid @RequestBody ExamDto req) {
        ResponseWrapper<Exam> response = examService.updateExam(id, req);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/exam/{id}")
    public ResponseEntity<MessageResponse> deleteExam(@PathVariable Long id) {
        MessageResponse response = examService.deleteExam(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/exam/{id}")
    public ResponseEntity<Exam> getExamById(@PathVariable Long id) {
        Exam exam = examService.getExamById(id);
        return new ResponseEntity<>(exam, HttpStatus.OK);
    }
}
