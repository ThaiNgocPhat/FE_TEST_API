package com.ra.base_spring_boot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.base_spring_boot.model.base.BaseObject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseObject
{
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "birth_day")
    private String birthDay;

    @Column(name = "username")
    private String username;

    @Column(name = "avatar")
    private String avatar;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "otp")
    private String otp;

    @Column(name = "isVerified")
    private Boolean isVerified;

    @Column(name = "is_time_expired")
    private LocalDateTime isTimeExpired;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
