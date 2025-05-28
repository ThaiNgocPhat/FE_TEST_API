package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.ExamQuestionDTO;
import com.ra.base_spring_boot.dto.req.ExamWithSessionsDTO;
import com.ra.base_spring_boot.model.constants.SessionType;
import com.ra.base_spring_boot.security.principle.MyUserDetails;
import com.ra.base_spring_boot.services.IExamService;
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
public class ExamController {
    private final IExamService examService;

    @GetMapping("/exams/{examId}")
    public ResponseEntity<ResponseWrapper<ExamWithSessionsDTO>> getExamWithSessions(@AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable Long examId){
        ResponseWrapper<ExamWithSessionsDTO> response = examService.getExamWithSessions(myUserDetails, examId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/exams/{examId}/do/{sessionType}")
    public ResponseEntity<ResponseWrapper<List<ExamQuestionDTO>>> getExamQuestions(@PathVariable Long examId, @PathVariable SessionType sessionType){
        ResponseWrapper<List<ExamQuestionDTO>> response = examService.getExamQuestions(examId, sessionType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}