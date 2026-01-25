package com.community.demo.controller;

import com.community.demo.dto.UserDTO;
import com.community.demo.entity.User;
import com.community.demo.security.CustomAuthUser;
import com.community.demo.service.EmailService;
import com.community.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
@Controller
public class UserController {
    private final EmailService emailService;
    private final UserService userService;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @PostMapping("/send-verification")
    public org.springframework.http.ResponseEntity<Void> sendVerification(
            @org.springframework.web.bind.annotation.RequestParam String email) {
        emailService.sendVerificationCode(email);
        return org.springframework.http.ResponseEntity.ok().build();
    }

    @PostMapping("/verify-code")
    public org.springframework.http.ResponseEntity<Boolean> verifyCode(
            @org.springframework.web.bind.annotation.RequestParam String email,
            @org.springframework.web.bind.annotation.RequestParam String code,
            HttpServletRequest request) {
        boolean verified = emailService.verifyCode(email, code);
        if (verified) {
            request.getSession().setAttribute("verifiedEmail", email);
        }
        return org.springframework.http.ResponseEntity.ok(verified);
    }

    @PostMapping("/reset-password")
    public String resetPassword(@org.springframework.web.bind.annotation.RequestParam String email,
            @org.springframework.web.bind.annotation.RequestParam String pwd,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        String verifiedEmail = (String) request.getSession().getAttribute("verifiedEmail");

        if (verifiedEmail == null || !verifiedEmail.equals(email)) {
            redirectAttributes.addFlashAttribute("error", "이메일 인증이 필요합니다.");
            return "redirect:/user/password";
        }

        // 비밀번호 변경 로직 (Service에 위임하거나 직접 처리)
        // 여기서는 Service에 modify 메서드를 활용하거나 별도 메서드 추가가 필요함.
        // 편의상 Service에 비밀번호 변경 메서드를 추가하는 것이 좋음.
        // 임시로 userRepo를 직접 쓰지 않고 서비스에 위임하기 위해 DTO 사용
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setPwd(pwd);
        userService.modify(userDTO); // modify 메서드가 비밀번호 암호화를 수행하는지 확인 필요 -> 수행함.

        request.getSession().removeAttribute("verifiedEmail");
        redirectAttributes.addFlashAttribute("msg", "비밀번호가 변경되었습니다.");
        return "redirect:/user/login";
    }

    @GetMapping("/join")
    public void join() {
    }

    @GetMapping("/login")
    public void login(HttpServletRequest request,
            Model model) {
        String email = (String) request.getSession().getAttribute("email");
        String errMsg = (String) request.getSession().getAttribute("errMsg");
        if (errMsg != null) {
            log.info(">>> errMsg >> {}", errMsg);
            model.addAttribute("email", email);
            model.addAttribute("errMsg", errMsg);
        }
        request.getSession().removeAttribute("email");
        request.getSession().removeAttribute("errMsg");
    }

    @PostMapping("/join")
    public String join(UserDTO userDTO, HttpServletRequest request) {
        // 세션에서 인증된 이메일 확인
        String verifiedEmail = (String) request.getSession().getAttribute("verifiedEmail");
        if (verifiedEmail == null || !verifiedEmail.equals(userDTO.getEmail())) {
            return "redirect:/user/join?error=unverified";
        }

        String email = userService.register(userDTO);
        request.getSession().removeAttribute("verifiedEmail"); // 인증 정보 삭제
        log.info(">>> email >> {}", email);
        return "redirect:/";
    }

    @GetMapping("/list")
    public String list(Model model) {
        log.info(">>> Admin User List Page 접속");

        // Service를 통해 데이터 확보 (Entity 리스트 반환)
        List<User> userList = userService.getList();

        model.addAttribute("userList", userList);
        return "user/list"; // templates/user/list.html
    }

    @GetMapping("/password")
    public String password() {
        return "user/password";
    }

    @GetMapping("/charts")
    public void charts() {
    }

    @GetMapping("/modify")
    public String modify(@AuthenticationPrincipal CustomAuthUser customUser, Model model) {
        // 1. 로그인이 안 되어 있으면 customUser는 null입니다.
        if (customUser == null) {
            return "redirect:/user/login";
        }

        // 2. 로그는 찍히는지 확인 (터미널/콘솔 확인용)
        System.out.println("로그인 유저 정보: " + customUser.getUser());

        // 3. ★ 핵심: HTML에서 쓸 "user"라는 이름으로 데이터를 담아 보냄
        // 여기서 담는 객체는 entity.User 객체여야 합니다.
        model.addAttribute("user", customUser.getUser());

        return "user/modify";
    }

}
