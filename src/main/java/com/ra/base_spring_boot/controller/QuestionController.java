package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.QuestionDto;
import com.ra.base_spring_boot.model.Question;
import com.ra.base_spring_boot.model.constants.SessionType;
import com.ra.base_spring_boot.services.IQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class QuestionController {
    private final IQuestionService questionService;

    @PostMapping("/questions")
    public ResponseEntity<ResponseWrapper<Question>> createQuestion(@Valid @RequestBody QuestionDto req){
        ResponseWrapper<Question> response = questionService.addQuestion(req);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/questions")
    public ResponseEntity<ResponseWrapper<List<Question>>> getQuestionsByExamAndSession(
            @RequestParam Long examId,
            @RequestParam SessionType sessionType) {
        ResponseWrapper<List<Question>> response = questionService.getQuestionsByExamAndSession(examId, sessionType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<ResponseWrapper<Question>> updateQuestion(
            @PathVariable Long id,
            @RequestBody QuestionDto req) {
        ResponseWrapper<Question> response = questionService.updateQuestion(id, req);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteQuestion(@PathVariable Long id) {
        ResponseWrapper<String> response = questionService.deleteQuestion(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/questions/search")
    public ResponseEntity<ResponseWrapper<List<Question>>> searchQuestions(@RequestParam String keyword) {
        ResponseWrapper<List<Question>> response = questionService.searchQuestions(keyword);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
