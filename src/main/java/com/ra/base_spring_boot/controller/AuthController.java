package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.MessageResponse;
import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.ExamNameDTO;
import com.ra.base_spring_boot.dto.req.FormLogin;
import com.ra.base_spring_boot.dto.req.FormRegister;
import com.ra.base_spring_boot.dto.req.OtpDto;
import com.ra.base_spring_boot.dto.resp.JwtResponse;
import com.ra.base_spring_boot.services.IAuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final IAuthService authService;

    /**
     * @param formLogin FormLogin
     * @apiNote handle login with { username , password }
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper<JwtResponse>> login(@Valid @RequestBody FormLogin formLogin)
    {
        ResponseWrapper<JwtResponse> responseWrapper = authService.login(formLogin);
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    /**
     * @param formRegister FormRegister
     * @apiNote handle register with { fullName , username , password }
     */
    @PostMapping("/register")
    public ResponseEntity<?> handleRegister(@Valid @RequestBody FormRegister formRegister) throws MessagingException {
        authService.register(formRegister);
        return ResponseEntity.created(URI.create("api/v1/auth/register")).body(
                ResponseWrapper.builder()
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .data("Register successfully")
                        .build()
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<MessageResponse> handleVerify(@RequestBody OtpDto otp) {
        MessageResponse response = authService.verify(otp);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/exam-names")
    public ResponseEntity<ResponseWrapper<List<ExamNameDTO>>> getAllExamNames() {
        ResponseWrapper<List<ExamNameDTO>> examNames = authService.getAllExamNames();
        return new ResponseEntity<>(examNames, HttpStatus.OK);
    }
}
