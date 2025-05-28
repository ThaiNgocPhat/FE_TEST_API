package com.ra.base_spring_boot.services;
import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.UserAnswerDto;
import com.ra.base_spring_boot.dto.req.UserExamResultDto;
import com.ra.base_spring_boot.model.constants.SessionType;
import com.ra.base_spring_boot.security.principle.MyUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

public interface IUserAnswerService {
    ResponseWrapper<String> submitAnswers(Long userId, List<UserAnswerDto> userAnswers);
    ResponseWrapper<UserExamResultDto> getUserExamResult(Long userId, Long examId, Long sessionId, SessionType sessionType);
}
