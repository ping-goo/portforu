package org.pinggu.portforu.emailing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendClosingSoonNotification(String to, String jobTitle, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[PortForU] 관심 공고 마감 임박 알림");
        message.setText(String.format("스크랩하신 '%s' 공고가 곧 마감됩니다.\n지금 확인해보세요: %s", jobTitle, link));

        mailSender.send(message);
        log.info("Closing Soon 이메일 발송 완료: to={}, jobTitle={}, link={}", to, jobTitle, link);
    }
}
