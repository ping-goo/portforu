package org.pinggu.portforu.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.domain.payment.entity.Payment;
import org.pinggu.portforu.domain.payment.repository.PaymentRepository;
import org.pinggu.portforu.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/toss/webhook")
public class TossWebhookController {

    private final PaymentRepository paymentRepository;

    @PostMapping("/payment")
    public ResponseEntity<Void> paymentCompleted(@RequestBody Map<String, Object> webhookData) {
        String orderId = webhookData.get("orderId").toString();
        String status = webhookData.get("status").toString();

        Long subscribeId = Long.parseLong(orderId.replace("subscribe_", ""));

        Payment payment = paymentRepository.findBySubscribeId(subscribeId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."));

        if ("DONE".equals(status)) {
            payment.complete(); // 결제 완료 처리
        } else {
            payment.fail(); // 결제 실패 처리
        }

        paymentRepository.save(payment); // 결제 상태 저장

        return ResponseEntity.ok().build();
    }
}


