package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.ExamSessionScoreDto;
import com.ra.base_spring_boot.security.principle.MyUserDetails;
import com.ra.base_spring_boot.services.IExamResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class ExamResultController {
    private final IExamResultService examResultService;

    @GetMapping("/{examId}")
    public ResponseEntity<ResponseWrapper<List<ExamSessionScoreDto>>> getUserExamResult(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @PathVariable Long examId
    ) {
        ResponseWrapper<List<ExamSessionScoreDto>> response = examResultService.getResultsByUserAndExam(myUserDetails, examId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}