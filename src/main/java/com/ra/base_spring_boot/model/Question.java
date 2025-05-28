package com.ra.base_spring_boot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ra.base_spring_boot.model.base.BaseObject;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Question extends BaseObject {
    private String content;

    @ElementCollection
    private List<String> options;

    private int correctAnswerIndex;

    @ManyToOne
    @JoinColumn(name = "exam_session_id")
    @JsonBackReference
    private ExamSession examSession;
}
