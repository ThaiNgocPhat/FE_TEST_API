package com.ra.base_spring_boot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ra.base_spring_boot.model.base.BaseObject;
import com.ra.base_spring_boot.model.constants.SessionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class ExamSession extends BaseObject {
    @ManyToOne
    @JoinColumn(name = "exam_id")
    @JsonBackReference
    private Exam exam;

    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    @OneToMany(mappedBy = "examSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Question> questions;
}
