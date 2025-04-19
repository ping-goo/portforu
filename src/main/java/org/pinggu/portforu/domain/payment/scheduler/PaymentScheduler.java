package org.pinggu.portforu.domain.payment.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pinggu.portforu.domain.payment.entity.Payment;
import org.pinggu.portforu.domain.payment.enums.PaymentStatus;
import org.pinggu.portforu.domain.payment.repository.PaymentRepository;
import org.pinggu.portforu.domain.subscribe.entity.Subscribe;
import org.pinggu.portforu.domain.subscribe.repository.SubscribeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentScheduler {

    private final PaymentRepository paymentRepository;
    private final SubscribeRepository subscribeRepository;

    @Scheduled(cron = "0 */5 * * * *") // 5분마다
    @Transactional
    public void expireUnpaidPayments() {
        Instant limit = Instant.now().minus(Duration.ofMinutes(20));

        // PENDING 상태이고, 20분 이상 지난 것 전부 만료 처리
        List<Payment> expiredPayments = paymentRepository
                .findByStatusAndCreatedAtBefore(PaymentStatus.PENDING, limit);

        for (Payment payment : expiredPayments) {
            payment.expire();
            paymentRepository.save(payment);

            Subscribe subscribe = payment.getSubscribe();
            subscribe.fail();
            subscribeRepository.save(subscribe);

            log.info("[결제 만료 처리] paymentId={}, subscribeId={}, createdAt={}",
                    payment.getId(), subscribe.getId(), payment.getCreatedAt());
        }
    }

}