package org.pinggu.portforu.domain.payment.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pinggu.portforu.domain.payment.enums.PaymentStatus;
import org.pinggu.portforu.domain.payment.repository.PaymentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentScheduler {

    private final PaymentRepository paymentRepository;

    @Scheduled(cron = "0 */5 * * * *") // 5분마다
    @Transactional
    public void expireUnpaidPayments() {
        Instant limit = Instant.now().minus(Duration.ofMinutes(20));

        int updatedCount = paymentRepository.bulkExpireOldPendingPayments(
                PaymentStatus.EXPIRED,
                PaymentStatus.PENDING,
                limit
        );

        log.info("[결제 만료 처리] 만료된 결제 수: {}", updatedCount);
    }
}