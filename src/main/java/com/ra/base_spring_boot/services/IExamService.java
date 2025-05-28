package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.MessageResponse;
import com.ra.base_spring_boot.dto.PaginatedResponse;
import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.ExamDto;
import com.ra.base_spring_boot.dto.req.ExamQuestionDTO;
import com.ra.base_spring_boot.dto.req.ExamWithSessionsDTO;
import com.ra.base_spring_boot.model.Exam;
import com.ra.base_spring_boot.model.constants.SessionType;
import com.ra.base_spring_boot.security.principle.MyUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

public interface IExamService {
    ResponseWrapper<Exam> createExam(ExamDto req);
    PaginatedResponse<Exam> getExams(int page, int limit);
    ResponseWrapper<Exam> updateExam(Long id, ExamDto req);
    MessageResponse deleteExam(Long id);
    Exam getExamById(Long id);
    ResponseWrapper<ExamWithSessionsDTO> getExamWithSessions(@AuthenticationPrincipal MyUserDetails myUserDetails, Long examId);
    ResponseWrapper<List<ExamQuestionDTO>> getExamQuestions(Long examId, SessionType sessionType);

}
