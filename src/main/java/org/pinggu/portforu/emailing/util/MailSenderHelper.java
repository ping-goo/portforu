package org.pinggu.portforu.emailing.util;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pinggu.portforu.domain.member.entity.Member;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;


@Slf4j
@Component
@RequiredArgsConstructor
public class MailSenderHelper {

    private final JavaMailSender mailSender;
    private final SlackNotifier slackNotifier;

    // HTML 메일용
    public void sendIfSubscribed(Member member, BiConsumer<MimeMessageHelper, Member> messageBuilder) {
        if (!member.getIsEmailSubscribed()) {
            log.info("이메일 수신 거부한 회원입니다. 발송 스킵: {}", member.getEmail());
            return;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setTo(member.getEmail());

            // 외부에서 제목/본문 구성
            messageBuilder.accept(helper, member);

            mailSender.send(mimeMessage);
            log.info("이메일 발송 완료 (HTML): to={}, subject={}", member.getEmail(), helper.getMimeMessage().getSubject());

        } catch (Exception e) {
            log.error("이메일 발송 실패: to={}, error={}", member.getEmail(), e.getMessage(), e);
            slackNotifier.send("[메일 발송 실패]\n수신자: " + member.getEmail() + "\n오류: " + e.getMessage());
        }
    }

    // Unsubscribe 링크 HTML 포맷
    public String appendUnsubscribeLink(String originalHtml, Member member) {
        String token = member.getUnsubscribeToken();
        String unsubscribeUrl = "https://portforu.online/emails/unsubscribe?token=" + token;
        return originalHtml + "<br><br><a href=\"" + unsubscribeUrl + "\">[구독 해지하기]</a>";
    }
}
