package org.pinggu.portforu.emailing.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pinggu.portforu.domain.subscribe.repository.SubscribeRepository;
import org.pinggu.portforu.emailing.service.MailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlFinishNotificationListener {

    private final MailService mailService;
    private final SubscribeRepository subscribeRepository;

    @RabbitListener(queues = "crawl.complete.queue")
    public void handleCrawlFinished(@Payload Map<String, Object> message) {
        log.info("[RabbitMQ] 크롤링 완료 메시지 수신: {}", message);

        String site = (String) message.getOrDefault("site", "unknown");
        Integer crawledCount = (Integer) message.getOrDefault("crawledCount", 0);

        // 오늘 날짜 구하기
        LocalDate today = LocalDate.now();
        String todayFormatted = today.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));

        // 멤버십 유효 구독자 이메일 조회
        List<String> subscribedEmails = subscribeRepository.findAllActiveSubscribedMemberEmails();

        for (String email : subscribedEmails) {
            mailService.sendNewJobPostingNotification(
                    email,
                    "[PortForU]" + todayFormatted + "채용공고 업데이트",
                    "안녕하세요 회원님.\n\n" +
                            "오늘 총 " + crawledCount + " 건의 채용공고가 업데이트 되었습니다.\n" +
                            "지금 새로운 공고를 확인해보세요.\n\n" +
                            "감사합니다."
            );
        }

        log.info("[RabbitMQ] 멤버십 구독자 {}명에게 메일 발송 완료", subscribedEmails.size());
    }
}
