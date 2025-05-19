package com.ra.base_spring_boot.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ra.base_spring_boot.model.base.BaseObject;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Exam extends BaseObject {
    @Column(unique = true)
    private String examName;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ExamSession> sessions;
}
