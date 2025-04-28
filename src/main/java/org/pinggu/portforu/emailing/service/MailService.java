package org.pinggu.portforu.emailing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pinggu.portforu.emailing.SlackNotifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final SlackNotifier slackNotifier;

    // 채용공고 마감 메일링
    public void sendClosingSoonNotification(String to, String jobTitle, String link) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("[PortForU] 관심 공고 마감 임박 알림");
            message.setText(String.format("스크랩하신 '%s' 공고가 곧 마감됩니다.\n지금 확인해보세요: %s", jobTitle, link));

            mailSender.send(message);
            log.info("Closing Soon 이메일 발송 완료: to={}, jobTitle={}, link={}", to, jobTitle, link);
        } catch (Exception e) {
            log.error("Closing Soon 이메일 발송 실패: to={}, jobTitle={}, error={}", to, jobTitle, e.getMessage(), e);
            slackNotifier.send("[메일 발송 실패] 관심 공고 마감 알림 실패\n수신자: " + to + "\n공고 제목: " + jobTitle + "\n오류: " + e.getMessage());
        }
    }

    // 댓글 알림 메일링
    public void sendCommentNotification(String to, String portfolioTitle, String commentContent) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("[PortForU] 새 댓글이 달렸습니다!");
            message.setText(String.format("회원님의 포트폴리오 '%s'에 새로운 댓글이 작성되었습니다.\n\n댓글 내용:\n%s", portfolioTitle, commentContent));

            mailSender.send(message);
            log.info("댓글 알림 이메일 발송 완료: to={}, portfolioTitle={}", to, portfolioTitle);
        } catch (Exception e) {
            log.error("댓글 알림 이메일 발송 실패: to={}, portfolioTitle={}, error={}", to, portfolioTitle, e.getMessage(), e);
            slackNotifier.send("[메일 발송 실패] 댓글 알림 실패\n수신자: " + to + "\n포트폴리오 제목: " + portfolioTitle + "\n오류: " + e.getMessage());
        }
    }

    // 채용고고 업데이트 메일링(멤버십 대상)
    public void sendNewJobPostingNotification(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("신규 채용공고 이메일 발송 완료: to={}, subject={}", to, subject);
        } catch (Exception e) {
            log.error("신규 채용공고 이메일 발송 실패: to={}, subject={}, error={}", to, subject, e.getMessage(), e);
            slackNotifier.send("[메일 발송 실패] 신규 채용공고 알림 실패\n수신자: " + to + "\n제목: " + subject + "\n오류: " + e.getMessage());
        }
    }
}
