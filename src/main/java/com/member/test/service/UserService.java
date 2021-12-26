package com.member.test.service;

import com.member.test.domain.User;
import com.member.test.domain.UserRole;
import com.member.test.dto.SaveRequestDto;
import com.member.test.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void joinrUser(SaveRequestDto requestDto) {

        Optional<User> userName = userRepository.findByUsername(requestDto.getUsername());
        if(userName.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        requestDto.setRole(UserRole.USER);
        if(requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            requestDto.setRole(UserRole.ADMIN);
        }
        userRepository.save(requestDto.toEntity());
    }
}
