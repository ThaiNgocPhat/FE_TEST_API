package com.ra.base_spring_boot.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExamDto {
    @NotBlank(message = "Exam name is required")
    private String examName;
}
