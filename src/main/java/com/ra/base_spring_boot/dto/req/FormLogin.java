package com.ra.base_spring_boot.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormLogin {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 4, max = 100, message = "Tên đăng nhập phải từ 4 đến 100 ký tự")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, max = 32, message = "Mật khẩu phải từ 8 đến 32 ký tự")
    private String password;
}

