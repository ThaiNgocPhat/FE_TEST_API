package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.ExamSession;
import com.ra.base_spring_boot.model.constants.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IExamSessionRepository extends JpaRepository<ExamSession, Long> {
    List<ExamSession> findByExamIdAndSessionType(Long examId, SessionType sessionType);
}
