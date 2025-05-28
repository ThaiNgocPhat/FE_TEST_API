package com.ra.base_spring_boot.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswerDetailDto {
    private String questionContent;
    private List<String> options;
    private int selectedAnswerIndex;
    private int correctAnswerIndex;
    private boolean isCorrect;
}
