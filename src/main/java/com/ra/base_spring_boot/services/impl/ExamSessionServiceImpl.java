package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.model.Exam;
import com.ra.base_spring_boot.model.ExamSession;
import com.ra.base_spring_boot.model.constants.SessionType;
import com.ra.base_spring_boot.repository.IExamSessionRepository;
import com.ra.base_spring_boot.services.IExamSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamSessionServiceImpl implements IExamSessionService {
    private final IExamSessionRepository examSessionRepository;

    @Override
    public void createDefaultSessionsForExam(Exam exam) {
        if(exam.getSessions() != null && !exam.getSessions().isEmpty()){
            return;
        }
        ExamSession morning = ExamSession.builder()
                .exam(exam)
                .sessionType(SessionType.MORNING)
                .build();
        ExamSession afternoon = ExamSession.builder()
                .exam(exam)
                .sessionType(SessionType.AFTERNOON)
                .build();
        examSessionRepository.saveAll(List.of(morning, afternoon));
    }
}
