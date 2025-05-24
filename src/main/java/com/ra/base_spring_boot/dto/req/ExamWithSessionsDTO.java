package com.ra.base_spring_boot.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamWithSessionsDTO {
    private Long id;
    private String examName;
    private List<SessionInfoDTO> sessions;
}
