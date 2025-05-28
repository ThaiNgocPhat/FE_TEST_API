package com.ra.base_spring_boot.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExamResultDto {
    private Long examId;
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private List<UserAnswerDetailDto> answers;
}

