package com.member.test.service;

import com.member.test.domain.User;
import com.member.test.domain.UserRole;
import com.member.test.dto.SaveRequestDto;
import com.member.test.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public void joinUser(SaveRequestDto requestDto) {

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

    /*@Transactional
    public User editUser(SaveRequestDto requestDto) {
        Long id = requestDto.getId();
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        String password = requestDto.toEntity().getPassword();
        String email = requestDto.getEmail();
        int result = userRepository.editByUser(id, password, email);

        if(result == 0) {
            throw new IllegalArgumentException("해당 유저의 정보 수정이 실패하였습니다.  userID = " + id);
        }

        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userID = " + id));
    }
*/
    @Transactional
    public void editUser(SaveRequestDto requestDto) {
        Long id = requestDto.getId();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();

        int result = userRepository.editByUser(id, password, email);
        if(result <= 0) {
            throw new IllegalArgumentException("해당 유저의 정보 수정이 실패하였습니다. userID = " + id);
        }

        User userEntity = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저의 정보를 찾을 수 없습니다.  userID = " + id));

        // 세션 등록
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEntity.getUsername(), requestDto.getPassword()));
        System.out.println(authentication.toString());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
