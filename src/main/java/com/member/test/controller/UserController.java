package com.member.test.controller;

import com.member.test.dto.SaveRequestDto;
import com.member.test.security.UserDetailsImpl;
import com.member.test.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 메인 페이지
    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails != null) {
            model.addAttribute("user", userDetails.getUser());
        }
        return "index";
    }

    // 회원 로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    // 회원 가입 페이지
    @GetMapping("/join")
    public String signup() {
        return "join";
    }

    // 회원 가입 요청 처리
    @PostMapping("/join")
    public String registerUser(SaveRequestDto requestDto) {
        userService.joinUser(requestDto);
        return "redirect:/";
    }

    // 마이페이지
    @GetMapping("/user/info")
    public String userInfo(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails != null) {
            model.addAttribute("user", userDetails.getUser());
        }
        return "myPage";
    }

    // 회원정보 수정 페이지
    @GetMapping("/user/edit")
    public String userEdit(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails != null) {
            model.addAttribute("user", userDetails.getUser());
        }
        return "myinfo-edit";
    }

    // 회원정보 수정
    @PutMapping("/user/edit")
    public String userEdit(SaveRequestDto requestDto) {
//        User userEntity = userService.editUser(requestDto);
//        userDetails.setUser(userEntity);
        userService.editUser(requestDto);

        return "redirect:/user/info";
    }

}
