package com.ra.base_spring_boot.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExamQuestionDTO {
    private Long id;
    private String content;
    private List<String> options;
}
