package com.example.tastyhub.common.verified.email.service;

import com.example.tastyhub.common.domain.user.repository.UserRepository;
import com.example.tastyhub.common.utils.Redis.RedisUtil;
import com.example.tastyhub.common.verified.email.dtos.AuthEmailRequest;
import com.example.tastyhub.common.verified.email.dtos.VerifiedEmailRequest;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final RedisUtil redisUtil;

    private final UserRepository userRepository;
    private static int authNumber;

    @Value("${spring.mail.username}")
    private String sender;


    private static void createNumber() {
        authNumber =  (int) ((Math.random() * (900000)) + 1000000);
    }

    public MimeMessage createMail(String mail){
        createNumber();  // 인증 코드 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(sender);   // 보내는 이메일
            message.setRecipients(RecipientType.TO, mail); // 보낼 이메일 설정
            message.setSubject("[TastyHub] 회원가입을 위한 이메일 인증");  // 제목 설정
            String body = "";
            body += "<h1>" + "안녕하세요." + "</h1>";
            body += "<h1>" + "TastyHub 입니다." + "</h1>";
            body += "<h3>" + "회원가입을 위한 요청하신 인증 번호입니다." + "</h3><br>";
            body += "<h2>" + "아래 코드를 회원가입 창으로 돌아가 입력해주세요." + "</h2>";

            body += "<div align='center' style='border:1px solid black; font-family:verdana;'>";
            body += "<h2>" + "회원가입 인증 코드입니다." + "</h2>";
            body += "<h1 style='color:blue'>" + authNumber + "</h1>";
            body += "</div><br>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
            redisUtil.saveData(mail, String.valueOf(authNumber), 60*30L);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    @Override
    public void authEmail(AuthEmailRequest authEmail) {
        String email = authEmail.getEmail();
        if(userRepository.existsByEmail(email)){
            throw new IllegalArgumentException("해당 이메일로 이미 가입된 사용자가 존재합니다.");
        }
        if (redisUtil.existData(email)){
            redisUtil.deleteData(email);
        }
        MimeMessage mail = createMail(email);
        javaMailSender.send(mail);
    }

    @Override
    public boolean verifiedEmail(VerifiedEmailRequest verifiedEmailRequest) {
        String email = verifiedEmailRequest.getEmail();
        String verifiedCode = verifiedEmailRequest.getVerifiedCode();

        String data = redisUtil.getData(email);
        if (data == null) {
            return false;
        }else{
            return checkVerifiedCode(verifiedCode, data);
        }
    }

    private boolean checkVerifiedCode(String verifiedCode, String data) {
        if (verifiedCode.equals(data)) {
            return true;
        }
        return false;
    }
}
