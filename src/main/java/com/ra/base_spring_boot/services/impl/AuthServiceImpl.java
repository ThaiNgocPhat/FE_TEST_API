package com.ra.base_spring_boot.services.impl;
import com.ra.base_spring_boot.dto.MessageResponse;
import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.*;
import com.ra.base_spring_boot.dto.resp.JwtResponse;
import com.ra.base_spring_boot.exception.HttpBadRequest;
import com.ra.base_spring_boot.exception.HttpConflict;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.Exam;
import com.ra.base_spring_boot.model.ExamSession;
import com.ra.base_spring_boot.model.Role;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.model.constants.RoleName;
import com.ra.base_spring_boot.model.constants.SessionType;
import com.ra.base_spring_boot.repository.IExamRepository;
import com.ra.base_spring_boot.repository.IRoleRepository;
import com.ra.base_spring_boot.repository.IUserRepository;
import com.ra.base_spring_boot.security.jwt.JwtProvider;
import com.ra.base_spring_boot.services.IAuthService;
import com.ra.base_spring_boot.services.IRoleService;
import com.ra.base_spring_boot.utils.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService
{
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final MailService mailService;
    private final IExamRepository examRepository;
    private final ModelMapper modelMapper;

    @Override
    public void register(FormRegister request) throws MessagingException {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new HttpConflict("Email or phone already exists");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new HttpBadRequest("Confirm Password Does Not Match, Please Try Again!");
        }
        Set<Role> roles = new HashSet<>();
        List<String> roleList = request.getRoles();
        if (roleList != null && !roleList.isEmpty()) {
            roleList.forEach(roleName -> {
                Role role = roleRepository.findByRoleName(RoleName.valueOf(roleName.toUpperCase()))
                        .orElseThrow(() -> new HttpNotFound("Role Not Found: " + roleName));
                roles.add(role);
            });
        } else {
            Role defaultRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new HttpNotFound("Default Role Not Found"));
            roles.add(defaultRole);
        }
        String otp = jwtProvider.generateOTP();
        User user = User.builder()
                .fullName(request.getFullName())
                .birthDay(request.getBirthDay())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .otp(otp)
                .status(false)
                .isVerified(false)
                .isTimeExpired(LocalDateTime.now())
                .build();
        user.setRoles(roles);
        user.setOtp(otp);
        userRepository.save(user);
        mailService.sendOTPEmail(
                user.getUsername(),
                user.getFullName(),
                user.getUsername(),
                otp
        );

    }

    @Override
    public ResponseWrapper<JwtResponse> login(FormLogin formLogin) {
        User user = userRepository.findByUsername(formLogin.getUsername())
                .orElseThrow(() -> new HttpBadRequest("User not found"));

        if (!passwordEncoder.matches(formLogin.getPassword(), user.getPassword())) {
            throw new HttpBadRequest("Wrong password");
        }

        if (user.getStatus() == null || !user.getStatus()) {
            throw new HttpBadRequest("Account is inactive");
        }

        if (user.getIsVerified() == null || !user.getIsVerified()) {
            throw new HttpBadRequest("Account is not verified");
        }

        Set<Role> roles = user.getRoles();
        if (roles == null || roles.isEmpty()) {
            throw new HttpBadRequest("User has no roles");
        }

        String token = jwtProvider.generateToken(user.getUsername(), roles);

        JwtResponse response = new JwtResponse();
        response.setToken(token);

        ResponseWrapper<JwtResponse> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setStatus(HttpStatus.OK);
        responseWrapper.setCode(200);
        responseWrapper.setData(response);

        return responseWrapper;
    }

    @Override
    public MessageResponse verify(OtpDto otp) {
        User user = userRepository.findByOtp(otp.getOtp())
                .orElseThrow(() -> new HttpBadRequest("Invalid OTP"));

        if (user.getIsVerified()) {
            throw new HttpBadRequest("User is already verified");
        }

        if (otp.getOtp() == null || !otp.getOtp().trim().equals(user.getOtp().trim())) {
            throw new HttpBadRequest("OTP is invalid");
        }

        user.setIsVerified(true);
        user.setStatus(true);
        user.setOtp(null);
        userRepository.save(user);

        MessageResponse response = new MessageResponse();
        response.setMessage("User is verified");
        return response;
    }

    @Override
    public ResponseWrapper<List<ExamNameDTO>> getAllExamNames() {
        List<Exam> exams = examRepository.findAll();

        List<ExamNameDTO> examNames = exams.stream()
                .map(exam -> new ExamNameDTO(exam.getId(), exam.getExamName()))
                .collect(Collectors.toList());

        ResponseWrapper<List<ExamNameDTO>> response = new ResponseWrapper<>();
        response.setCode(200);
        response.setStatus(HttpStatus.OK);
        response.setData(examNames);
        return response;
    }

    @Override
    public ResponseWrapper<ExamWithSessionsDTO> getExamWithSessions(Long examId) {
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
