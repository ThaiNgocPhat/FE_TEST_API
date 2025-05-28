package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.MessageResponse;
import com.ra.base_spring_boot.dto.PaginatedResponse;
import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.ExamDto;
import com.ra.base_spring_boot.dto.req.ExamQuestionDTO;
import com.ra.base_spring_boot.dto.req.ExamWithSessionsDTO;
import com.ra.base_spring_boot.dto.req.SessionInfoDTO;
import com.ra.base_spring_boot.exception.HttpConflict;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.Exam;
import com.ra.base_spring_boot.model.ExamSession;
import com.ra.base_spring_boot.model.constants.SessionType;
import com.ra.base_spring_boot.repository.IExamRepository;
import com.ra.base_spring_boot.security.principle.MyUserDetails;
import com.ra.base_spring_boot.services.IExamService;
import com.ra.base_spring_boot.services.IExamSessionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements IExamService {
    private final IExamRepository examRepository;
    private final ModelMapper modelMapper;
    private final IExamSessionService examSessionService;

    @Override
    public ResponseWrapper<Exam> updateExam(Long id, ExamDto req) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Exam not found with id: " + id));
        List<Exam> existingExams = examRepository.findByExamName(req.getExamName());
        for (Exam existing : existingExams) {
            if (!existing.getId().equals(id)) {
                throw new HttpConflict("Exam name already exists");
            }
        }
        new ModelMapper().map(req, exam);
        Exam updatedExam = examRepository.save(exam);
        ResponseWrapper<Exam> response = new ResponseWrapper<>();
        response.setCode(200);
        response.setStatus(HttpStatus.OK);
        response.setData(updatedExam);
        return response;
    }


    @Override
    public PaginatedResponse<Exam> getExams(int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<Exam> exams = examRepository.findAll(pageRequest);
        PaginatedResponse<Exam> response = new PaginatedResponse<>();
        response.setCurrentPage(exams.getNumber());
        response.setPageSize(exams.getSize());
        response.setTotalElements(exams.getTotalElements());
        response.setTotalPages(exams.getTotalPages());
        response.setData(exams.getContent());
        return response;
    }


    @Override
    public ResponseWrapper<Exam> createExam(ExamDto req) {
        List<Exam> existingExams = examRepository.findByExamName(req.getExamName());
        if (!existingExams.isEmpty()) {
            throw new HttpConflict("Exam already exists with this name");
        }
        Exam exam = modelMapper.map(req, Exam.class);
        Exam addExam = examRepository.save(exam);
        examSessionService.createDefaultSessionsForExam(addExam);
        ResponseWrapper<Exam> response = new ResponseWrapper<>();
        response.setCode(201);
        response.setStatus(HttpStatus.CREATED);
        response.setData(addExam);
        return response;
    }

    @Override
    public MessageResponse deleteExam(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Exam not found with id: " + id));
        examRepository.delete(exam);
        MessageResponse response = new MessageResponse();
        response.setMessage("Exam deleted successfully");
        return response;
    }

    @Override
    public Exam getExamById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Exam not found with id: " + id));
    }

    @Override
    public ResponseWrapper<ExamWithSessionsDTO> getExamWithSessions(@AuthenticationPrincipal MyUserDetails myUserDetails, Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new HttpNotFound("Exam not found"));

        List<SessionInfoDTO> sessionDTOs = exam.getSessions().stream()
                .map(session -> new SessionInfoDTO(
                        session.getId(),
                        session.getSessionType().name()
                ))
                .collect(Collectors.toList());

        ExamWithSessionsDTO dto = new ExamWithSessionsDTO(
                exam.getId(),
                exam.getExamName(),
                sessionDTOs
        );

        ResponseWrapper<ExamWithSessionsDTO> response = new ResponseWrapper<>();
        response.setCode(200);
        response.setStatus(HttpStatus.OK);
        response.setData(dto);
        return response;
    }

    @Override
    public ResponseWrapper<List<ExamQuestionDTO>> getExamQuestions(Long examId, SessionType sessionType) {
        Optional<Exam> examOpt = examRepository.findById(examId);

        if (examOpt.isEmpty()) {
            return ResponseWrapper.<List<ExamQuestionDTO>>builder()
                    .status(HttpStatus.NOT_FOUND)
                    .code(HttpStatus.NOT_FOUND.value())
                    .data(null)
                    .build();
        }

        Optional<ExamSession> sessionOpt = examOpt.get().getSessions().stream()
                .filter(s -> s.getSessionType() == sessionType)
                .findFirst();

        if (sessionOpt.isEmpty()) {
            return ResponseWrapper.<List<ExamQuestionDTO>>builder()
                    .status(HttpStatus.NOT_FOUND)
                    .code(HttpStatus.NOT_FOUND.value())
                    .data(null)
                    .build();
        }

        List<ExamQuestionDTO> questionDTOs = sessionOpt.get()
                .getQuestions()
                .stream()
                .map(q -> modelMapper.map(q, ExamQuestionDTO.class))
                .toList();

        ResponseWrapper<List<ExamQuestionDTO>> response = new ResponseWrapper<>();
        response.setCode(200);
        response.setStatus(HttpStatus.OK);
        response.setData(questionDTOs);
        return response;
    }
}
