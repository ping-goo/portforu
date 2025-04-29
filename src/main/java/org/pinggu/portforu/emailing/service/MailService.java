package org.pinggu.portforu.emailing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pinggu.portforu.domain.member.entity.Member;
import org.pinggu.portforu.emailing.util.MailSenderHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSenderHelper mailSenderHelper;

    // 채용공고 마감 메일링
    public void sendClosingSoonNotification(Member member, String jobTitle, String link) {
        mailSenderHelper.sendIfSubscribed(member, (helper, m) -> {  // helper: MimeMessageHelper
            try {
                helper.setSubject("[PortForU] 관심 공고 마감 임박 알림");
                String body = String.format("스크랩하신 '%s' 공고가 곧 마감됩니다.<br>지금 확인해보세요: %s", jobTitle, link);

                String fullBody = mailSenderHelper.appendUnsubscribeLink(body, m);
                helper.setText(fullBody, true); // true = HTML 모드
            } catch (Exception e) {
                log.error("메일 내용 구성 실패: {}", e.getMessage(), e);
            }
        });
    }

    // 댓글 알림 메일링
    public void sendCommentNotification(Member member, String portfolioTitle, String commentContent) {
        mailSenderHelper.sendIfSubscribed(member, (helper, m) -> {
            try {
                helper.setSubject("[PortForU] 새 댓글이 달렸습니다!");
                String body = String.format("회원님의 포트폴리오 '%s'에 새로운 댓글이 작성되었습니다.<br><br>댓글 내용:<br>%s", portfolioTitle, commentContent);

                String fullBody = mailSenderHelper.appendUnsubscribeLink(body, m);
                helper.setText(fullBody, true); // HTML 모드
            } catch (Exception e) {
                log.error("메일 내용 구성 실패: {}", e.getMessage(), e);
            }
        });
    }

    // 채용고고 업데이트 메일링(멤버십 대상)
    public void sendNewJobPostingNotification(Member member, String subject, String text) {
        mailSenderHelper.sendIfSubscribed(member, (helper, m) -> {
            try {
                helper.setSubject(subject);

                String fullBody = mailSenderHelper.appendUnsubscribeLink(text, m);
                helper.setText(fullBody, true); // HTML 모드
            } catch (Exception e) {
                log.error("메일 내용 구성 실패: {}", e.getMessage(), e);
            }
        });
    }

}
