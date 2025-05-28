package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.base.BaseObject;
import com.ra.base_spring_boot.model.constants.SessionType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class ExamResult extends BaseObject {

    @ManyToOne
    @JoinColumn(name = "exam_session_id")
    private ExamSession examSession;

    private Long userId;

    private int correctAnswers;

    private int totalQuestions;
    private int score;
    private SessionType sessionType;
}
