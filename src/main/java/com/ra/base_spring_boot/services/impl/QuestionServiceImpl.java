package com.ra.base_spring_boot.services.impl;
import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.QuestionDto;
import com.ra.base_spring_boot.exception.HttpBadRequest;
import com.ra.base_spring_boot.exception.HttpConflict;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.ExamSession;
import com.ra.base_spring_boot.model.Question;
import com.ra.base_spring_boot.model.constants.SessionType;
import com.ra.base_spring_boot.repository.IExamSessionRepository;
import com.ra.base_spring_boot.repository.IQuestionRepository;
import com.ra.base_spring_boot.services.IQuestionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements IQuestionService {
    private final IQuestionRepository questionRepository;
    private final IExamSessionRepository examSessionRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseWrapper<Question> addQuestion(QuestionDto req) {
        ExamSession session = examSessionRepository.findById(req.getExamSessionId())
                .orElseThrow(() -> new HttpNotFound("Exam session not found"));
        if (req.getOptions() == null || req.getOptions().size() != 4) {
            throw new HttpBadRequest("Options must contain exactly 4 answers");
        }

        if (req.getCorrectAnswerIndex() < 0 || req.getCorrectAnswerIndex() > 3) {
            throw new HttpBadRequest("Correct answer index must be between 0 and 3");
        }
        List<Question> existingQuestions = questionRepository.findByExamSessionIdAndContent(req.getExamSessionId(), req.getContent());
        if (!existingQuestions.isEmpty()) {
            throw new HttpConflict("A question with the same content already exists in this session");
        }
        Question question = modelMapper.map(req, Question.class);
        question.setExamSession(session);
        Question addQuestion = questionRepository.save(question);

        ResponseWrapper<Question> response = new ResponseWrapper<>();
        response.setCode(201);
        response.setStatus(HttpStatus.CREATED);
        response.setData(addQuestion);
        return response;
    }

    @Override
    public ResponseWrapper<List<Question>> getQuestionsByExamAndSession(Long examId, SessionType sessionType) {
        List<ExamSession> sessions = examSessionRepository.findByExamIdAndSessionType(examId, sessionType);
        if (sessions.isEmpty()) {
            throw new HttpNotFound("No exam session found for the given exam and session type");
        }
        List<Question> questions = questionRepository.findByExamSession(sessions.get(0));
        ResponseWrapper<List<Question>> response = new ResponseWrapper<>();
        response.setCode(200);
        response.setStatus(HttpStatus.OK);
        response.setData(questions);
        return response;
    }

    @Override
    public ResponseWrapper<Question> updateQuestion(Long id, QuestionDto req) {
        Question existing = questionRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Question not found"));

        if (req.getOptions() == null || req.getOptions().size() != 4) {
            throw new HttpBadRequest("Options must contain exactly 4 answers");
        }

        if (req.getCorrectAnswerIndex() < 0 || req.getCorrectAnswerIndex() > 3) {
            throw new HttpBadRequest("Correct answer index must be between 0 and 3");
        }

        existing.setContent(req.getContent());
        existing.setOptions(req.getOptions());
        existing.setCorrectAnswerIndex(req.getCorrectAnswerIndex());

        Question updated = questionRepository.save(existing);

        ResponseWrapper<Question> response = new ResponseWrapper<>();
        response.setCode(200);
        response.setStatus(HttpStatus.OK);
        response.setData(updated);
        return response;
    }

    @Override
    public ResponseWrapper<String> deleteQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Question not found"));

        questionRepository.delete(question);

        ResponseWrapper<String> response = new ResponseWrapper<>();
        response.setCode(200);
        response.setStatus(HttpStatus.OK);
        response.setData("Question deleted successfully");
        return response;
    }
    @Override
    public ResponseWrapper<List<Question>> searchQuestions(String keyword) {
        List<Question> questions = questionRepository.findByContentContaining(keyword);

        ResponseWrapper<List<Question>> response = new ResponseWrapper<>();
        response.setCode(200);
        response.setStatus(HttpStatus.OK);
        response.setData(questions);

        return response;
    }
}
