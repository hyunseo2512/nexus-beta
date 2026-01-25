package com.community.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    // 이메일 - {코드, 만료시간} 저장
    private final Map<String, VerificationInfo> verificationMap = new ConcurrentHashMap<>();

    // 5분 유효
    private static final long EXPIRATION_MINUTES = 5;

    public void sendVerificationCode(String toEmail) {
        String code = generateCode();

        // 메모리에 저장 (기존 코드가 있어도 덮어씌움)
        verificationMap.put(toEmail, new VerificationInfo(code, LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES)));

        // 실제 이메일 발송
        sendEmail(toEmail, "Community 회원가입 인증 코드", "인증 코드: " + code);

        log.info("Sent verification code to {}: {}", toEmail, code); // 개발 편의를 위해 로그 출력
    }

    public boolean verifyCode(String email, String code) {
        VerificationInfo info = verificationMap.get(email);

        if (info == null) {
            return false;
        }

        // 만료 확인
        if (LocalDateTime.now().isAfter(info.expirationTime)) {
            verificationMap.remove(email); // 만료된 코드 삭제
            return false;
        }

        // 코드 일치 확인
        if (info.code.equals(code)) {
            verificationMap.remove(email); // 인증 성공 시 삭제 (1회용)
            return true;
        }

        return false;
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email to {}", to, e);
            throw new RuntimeException("이메일 발송 실패", e);
        }
    }

    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6자리 난수
        return String.valueOf(code);
    }

    private record VerificationInfo(String code, LocalDateTime expirationTime) {
    }
}
