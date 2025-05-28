package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.model.Exam;

public interface IExamSessionService {
    void createDefaultSessionsForExam(Exam exam);
}
