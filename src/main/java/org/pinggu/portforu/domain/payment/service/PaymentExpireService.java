package org.pinggu.portforu.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pinggu.portforu.common.lock.RedisLockExecutor;
import org.pinggu.portforu.domain.payment.entity.Payment;
import org.pinggu.portforu.domain.payment.enums.PaymentStatus;
import org.pinggu.portforu.domain.payment.repository.PaymentRepository;
import org.pinggu.portforu.domain.subscribe.entity.Subscribe;
import org.pinggu.portforu.domain.subscribe.repository.SubscribeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentExpireService {

    private final PaymentRepository paymentRepository;
    private final SubscribeRepository subscribeRepository;
    private final RedisLockExecutor redisLockExecutor;

    @Transactional
    public void expireIfPending(Long subscribeId) {
        String lockKey = "lock:subscribe:" + subscribeId;

        redisLockExecutor.executeWithLock(lockKey, 5, 3, () -> {
            Optional<Payment> optionalPayment = paymentRepository.findBySubscribeId(subscribeId);

            optionalPayment.ifPresent(payment -> {
                if (payment.getStatus() == PaymentStatus.PENDING && payment.getPaymentKey() == null) {
                    payment.expire();
                    Subscribe subscribe = payment.getSubscribe();
                    subscribe.fail(); // 구독 상태 취소로 변경

                    paymentRepository.save(payment);
                    subscribeRepository.save(subscribe);

                    log.info("결제 만료 처리 완료: subscribeId={}", subscribeId);
                } else {
                    log.info("결제 만료 조건 미충족: status={}, key={}", payment.getStatus(), payment.getPaymentKey());
                }
            });
        });
    }
}