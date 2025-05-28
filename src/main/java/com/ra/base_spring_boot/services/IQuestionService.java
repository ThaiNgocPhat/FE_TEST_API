package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.QuestionDto;
import com.ra.base_spring_boot.model.Question;
import com.ra.base_spring_boot.model.constants.SessionType;

import java.util.List;

public interface IQuestionService {
    ResponseWrapper<Question> addQuestion(QuestionDto req);
    ResponseWrapper<List<Question>> getQuestionsByExamAndSession(Long examId, SessionType sessionType);
    ResponseWrapper<Question> updateQuestion(Long id, QuestionDto req);
    ResponseWrapper<String> deleteQuestion(Long id);
    ResponseWrapper<List<Question>> searchQuestions(String keyword);
}
