package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.ExamSessionScoreDto;
import com.ra.base_spring_boot.security.principle.MyUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

public interface IExamResultService {
    ResponseWrapper<List<ExamSessionScoreDto>> getResultsByUserAndExam(@AuthenticationPrincipal MyUserDetails myUserDetails, Long examId);
}

