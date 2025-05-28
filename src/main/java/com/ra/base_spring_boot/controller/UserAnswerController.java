package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.UserAnswerDto;
import com.ra.base_spring_boot.dto.req.UserExamResultDto;
import com.ra.base_spring_boot.model.constants.SessionType;
import com.ra.base_spring_boot.security.principle.MyUserDetails;
import com.ra.base_spring_boot.services.IUserAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserAnswerController {
    private final IUserAnswerService userAnswerService;

    @PostMapping("/submit-answers")
    public ResponseEntity<ResponseWrapper<String>> submitAnswers(@AuthenticationPrincipal MyUserDetails myUserDetails, @RequestBody List<UserAnswerDto> userAnswers) {
        Long userId = myUserDetails.getUser().getId();
        ResponseWrapper<String> response = userAnswerService.submitAnswers(userId, userAnswers);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/result/{examId}/{sessionId}/{sessionType}")
    public ResponseEntity<ResponseWrapper<UserExamResultDto>> getUserExamResult(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @PathVariable Long examId,
            @PathVariable Long sessionId,
            @PathVariable SessionType sessionType) {
        Long userId = myUserDetails.getUser().getId();
        ResponseWrapper<UserExamResultDto> response = userAnswerService.getUserExamResult(userId, examId, sessionId, sessionType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}