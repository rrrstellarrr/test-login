package com.member.test.dto;

import com.member.test.domain.User;
import com.member.test.domain.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveRequestDto {

    private Long id;
    private String username;
    private String password;
    private String email;
    private UserRole role;
    private boolean admin = false;
    private String adminToken = "";

    public User toEntity() {
        User user = User.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .role(role)
                .build();
        return user;
    }


}
