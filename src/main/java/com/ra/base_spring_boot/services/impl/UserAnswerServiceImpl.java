package com.ra.base_spring_boot.services.impl;
import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.UserAnswerDetailDto;
import com.ra.base_spring_boot.dto.req.UserAnswerDto;
import com.ra.base_spring_boot.dto.req.UserExamResultDto;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.ExamResult;
import com.ra.base_spring_boot.model.ExamSession;
import com.ra.base_spring_boot.model.Question;
import com.ra.base_spring_boot.model.UserAnswer;
import com.ra.base_spring_boot.model.constants.SessionType;
import com.ra.base_spring_boot.repository.IExamResultRepository;
import com.ra.base_spring_boot.repository.IExamSessionRepository;
import com.ra.base_spring_boot.repository.IQuestionRepository;
import com.ra.base_spring_boot.repository.IUserAnswerRepository;
import com.ra.base_spring_boot.services.IUserAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAnswerServiceImpl implements IUserAnswerService {
    private final IUserAnswerRepository userAnswerRepository;
    private final IQuestionRepository questionRepository;
    private final IExamResultRepository examResultRepository;
    private final IExamSessionRepository examSessionRepository;

    @Override
    public ResponseWrapper<String> submitAnswers(Long userId, List<UserAnswerDto> userAnswers) {
        List<UserAnswer> toSave = new ArrayList<>();
        LocalDateTime submittedTime = LocalDateTime.now();

        Long examSessionId = userAnswers.get(0).getExamSessionId();
        if (examSessionId == null) {
            throw new HttpNotFound("Exam session ID must not be null");
        }

        ExamSession examSession = examSessionRepository.findById(examSessionId)
                .orElseThrow(() -> new HttpNotFound("Exam session not found"));

        for (UserAnswerDto dto : userAnswers) {
            if (dto.getQuestionId() == null) {
                throw new HttpNotFound("Question ID must not be null");
            }

            Question question = questionRepository.findById(dto.getQuestionId())
                    .orElseThrow(() -> new HttpNotFound("Question not found"));

            boolean isCorrect = question.getCorrectAnswerIndex() == dto.getSelectedAnswerIndex();

            UserAnswer answer = UserAnswer.builder()
                    .userId(userId)
                    .question(question)
                    .selectedAnswerIndex(dto.getSelectedAnswerIndex())
                    .isCorrect(isCorrect)
                    .submittedAt(submittedTime)
                    .sessionType(dto.getSessionType())
                    .examSession(examSession)
                    .build();

            toSave.add(answer);
        }

        userAnswerRepository.saveAll(toSave);

        long correctAnswers = toSave.stream().filter(UserAnswer::isCorrect).count();
        int totalQuestions = toSave.size();

        SessionType sessionType = userAnswers.get(0).getSessionType();

        ExamResult examResult = ExamResult.builder()
                .userId(userId)
                .correctAnswers((int) correctAnswers)
                .totalQuestions(totalQuestions)
                .examSession(examSession)
                .sessionType(sessionType)
                .build();

        examResultRepository.save(examResult);

        ResponseWrapper<String> response = new ResponseWrapper<>();
        response.setCode(200);
        response.setStatus(HttpStatus.OK);
        response.setData("Submitted successfully");
        return response;
    }

    @Override
    public ResponseWrapper<UserExamResultDto> getUserExamResult(Long userId, Long examId, Long sessionId, SessionType sessionType) {
        List<Question> questions = questionRepository.findByExamSession_Id(sessionId);
        if (questions.isEmpty()) {
            throw new HttpNotFound("No questions found for the specified exam and session.");
        }

        int totalQuestions = questions.size();
        List<Long> questionIds = questions.stream().map(Question::getId).toList();

        List<UserAnswer> userAnswers = userAnswerRepository.findByUserAndQuestionIdsAndSessionType(
                userId, sessionId, questionIds, sessionType
        );

        if (userAnswers.isEmpty()) {
            throw new HttpNotFound("User has not submitted answers for this exam.");
        }

        Map<LocalDateTime, List<UserAnswer>> groupedByTime = userAnswers.stream()
                .collect(Collectors.groupingBy(UserAnswer::getSubmittedAt));

        LocalDateTime latestTime = groupedByTime.keySet().stream()
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new RuntimeException("Cannot determine latest submission time."));

        List<UserAnswer> latestAnswers = groupedByTime.get(latestTime);

        Map<Long, UserAnswer> userAnswerMap = latestAnswers.stream()
                .collect(Collectors.toMap(a -> a.getQuestion().getId(), a -> a));

        List<UserAnswerDetailDto> answerDetails = new ArrayList<>();
        int correctCount = 0;

        for (Question question : questions) {
            UserAnswer answer = userAnswerMap.get(question.getId());
            int selectedIndex = -1;
            boolean isCorrect = false;

            if (answer != null) {
                selectedIndex = answer.getSelectedAnswerIndex();
                isCorrect = answer.isCorrect();
                if (isCorrect) correctCount++;
            }

            answerDetails.add(new UserAnswerDetailDto(
                    question.getContent(),
                    question.getOptions(),
                    selectedIndex,
                    question.getCorrectAnswerIndex(),
                    isCorrect
            ));
        }

        int wrongCount = totalQuestions - correctCount;

        UserExamResultDto resultDto = new UserExamResultDto(
                examId,
                totalQuestions,
                correctCount,
                wrongCount,
                answerDetails
        );

        ResponseWrapper<UserExamResultDto> response = new ResponseWrapper<>();
        response.setCode(200);
        response.setStatus(HttpStatus.OK);
        response.setData(resultDto);
        return response;
    }
}