package com.ra.base_spring_boot.repository;
import com.ra.base_spring_boot.model.ExamSession;
import com.ra.base_spring_boot.model.Question;
import com.ra.base_spring_boot.model.constants.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IQuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByExamSessionIdAndContent(Long examSessionId, String content);
    List<Question> findByExamSession(ExamSession session);
    List<Question> findByContentContaining(String keyword);
    List<Question> findByExamSession_Exam_IdAndExamSession_SessionType(Long examId, SessionType sessionType);
    List<Question> findByExamSession_Id(Long examSessionId);
}
