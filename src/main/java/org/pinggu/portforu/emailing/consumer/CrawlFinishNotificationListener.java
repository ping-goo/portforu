package org.pinggu.portforu.emailing.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pinggu.portforu.domain.member.entity.Member;
import org.pinggu.portforu.domain.member.service.MemberService;
import org.pinggu.portforu.domain.subscribe.validator.SubscribeValidator;
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
    private final MemberService memberService;
    private final SubscribeValidator subscribeValidator;

    @RabbitListener(queues = "crawl.complete.queue")
    public void handleCrawlFinished(@Payload Map<String, Object> message) {
        log.info("[RabbitMQ] 크롤링 완료 메시지 수신: {}", message);

        String site = (String) message.getOrDefault("site", "unknown");
        Integer crawledCount = (Integer) message.getOrDefault("crawledCount", 0);

        // 오늘 날짜 구하기
        LocalDate today = LocalDate.now();
        String todayFormatted = today.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));

        // 이메일 수신 동의 조회
        List<Member> members = memberService.findAllEmailSubscribedMembers();
        int sentCount = 0;

        for (Member member : members) {
            if (subscribeValidator.isSubscribed(member.getId())) { // 활성 멤버십 구독자만 메일 발송
                mailService.sendNewJobPostingNotification(
                        member,
                        "[PortForU] " + todayFormatted + " 채용공고 업데이트",
                        "안녕하세요 회원님.\n\n" +
                                "오늘 총 " + crawledCount + " 건의 채용공고가 업데이트 되었습니다.\n" +
                                "지금 새로운 공고를 확인해보세요.\n\n" +
                                "감사합니다."
                );
                sentCount++;
            }
        }

        log.info("[RabbitMQ] 메일 발송 완료 (발송 대상: {}명)", sentCount);
    }
}
