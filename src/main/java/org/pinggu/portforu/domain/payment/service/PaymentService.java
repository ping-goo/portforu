package org.pinggu.portforu.domain.payment.service;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.common.exception.CustomException;
import org.pinggu.portforu.domain.payment.entity.Payment;
import org.pinggu.portforu.domain.payment.enums.PaymentStatus;
import org.pinggu.portforu.domain.payment.repository.PaymentRepository;
import org.pinggu.portforu.domain.subscribe.service.SubscribeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscribeService subscribeService;

    // 결제 완료 처리
    public void completePayment(Long subscribeId) {
        Payment payment = paymentRepository.findBySubscribeId(subscribeId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."));

        payment.complete(); // 결제 완료 처리
        paymentRepository.save(payment); // 결제 상태 저장

        // 결제 완료 상태에 따라 구독 상태 업데이트
        subscribeService.updateSubscriptionStatus(subscribeId, PaymentStatus.COMPLETED);
    }

    // 결제 실패 처리
    public void failPayment(Long subscribeId) {
        Payment payment = paymentRepository.findBySubscribeId(subscribeId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."));

        payment.fail(); // 결제 실패 처리
        paymentRepository.save(payment); // 결제 상태 저장

        // 결제 실패 상태에 따라 구독 상태 업데이트
        subscribeService.updateSubscriptionStatus(subscribeId, PaymentStatus.FAILED);
    }

    // 결제 만료 처리
    public void expirePayment(Long subscribeId) {
        Payment payment = paymentRepository.findBySubscribeId(subscribeId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."));

        payment.expire(); // 결제 만료 처리
        paymentRepository.save(payment); // 결제 상태 저장

        // 결제 만료 상태에 따라 구독 상태 업데이트
        subscribeService.updateSubscriptionStatus(subscribeId, PaymentStatus.EXPIRED);
    }
}

