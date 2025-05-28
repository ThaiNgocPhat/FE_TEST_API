package com.ra.base_spring_boot.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormRegister {
    @NotBlank(message = "Full name must not be blank")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Birth date must not be blank")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Birth date must be in format dd/MM/yyyy")
    private String birthDay;

    @NotBlank(message = "Username must not be blank")
    @Email(message = "Username must be a valid email")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must include uppercase, lowercase, number and special character"
    )
    private String password;

    @NotBlank(message = "Confirm password must not be blank")
    private String confirmPassword;

    private MultipartFile avatar;
    private List<String> roles;
}

