package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findByUserIdAndExamSession_Exam_IdOrderByIdDesc(Long userId, Long examId);
}
