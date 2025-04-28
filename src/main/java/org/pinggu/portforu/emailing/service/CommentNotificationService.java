package org.pinggu.portforu.emailing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pinggu.portforu.emailing.message.CommentCreatedEvent;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentNotificationService {

    private final MailService mailService;

    public void notifyCommentCreated(CommentCreatedEvent event) {
        log.info("[댓글 알림] 이메일 발송 준비: receiverEmail={}, portfolioTitle={}",
                event.getReceiverEmail(), event.getPortfolioTitle());

        mailService.sendCommentNotification(event.getReceiverEmail(), event.getPortfolioTitle(), event.getCommentContent());
    }

}
