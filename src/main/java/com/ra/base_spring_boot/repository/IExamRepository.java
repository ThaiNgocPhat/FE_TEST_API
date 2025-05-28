package com.ra.base_spring_boot.repository;
import com.ra.base_spring_boot.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByExamName(String examName);
}
