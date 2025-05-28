package com.ra.base_spring_boot.dto.req;

import com.ra.base_spring_boot.model.constants.SessionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAnswerDto {
    private Long userId;
    private Long questionId;
    private int selectedAnswerIndex;
    private Long examSessionId;
    private SessionType sessionType;
}
