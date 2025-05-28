package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.base.BaseObject;
import com.ra.base_spring_boot.model.constants.SessionType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class UserAnswer extends BaseObject {
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private int selectedAnswerIndex;

    private boolean isCorrect;

    private LocalDateTime submittedAt;

    private SessionType sessionType;

    @ManyToOne
    @JoinColumn(name = "exam_session_id")
    private ExamSession examSession;

}

