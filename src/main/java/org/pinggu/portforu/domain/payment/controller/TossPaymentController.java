package org.pinggu.portforu.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.common.dto.ApiResponse;
import org.pinggu.portforu.domain.payment.enums.PaymentStatus;
import org.pinggu.portforu.domain.payment.service.PaymentService;
import org.pinggu.portforu.domain.payment.service.TossPaymentLinkService;
import org.pinggu.portforu.domain.subscribe.entity.Subscribe;
import org.pinggu.portforu.domain.subscribe.service.SubscribeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class TossPaymentController {

    private final TossPaymentLinkService tossPaymentLinkService;
    private final PaymentService paymentService;
    private final SubscribeService subscribeService;

    @PostMapping
    public ResponseEntity<String> savePaymentLink(@RequestBody Subscribe subscribe) {
        // 결제 링크 생성
        String paymentLink = tossPaymentLinkService.savePaymentLink(subscribe);

        // 결제 링크 반환 (사용자가 클릭할 수 있도록 전달)
        return ResponseEntity.ok(paymentLink);
    }

    // 결제 완료 처리 (웹훅으로 호출)
    @PostMapping("/payment-complete")
    public ResponseEntity<ApiResponse<String>> paymentComplete(@RequestBody Map<String, Object> webhookData) {
        // Webhook에서 받은 데이터 처리
        String orderId = webhookData.get("orderId").toString();
        String status = webhookData.get("status").toString();

        Long subscribeId = Long.parseLong(orderId.replace("subscribe_", ""));

        // 결제 상태에 따라 처리
        if ("DONE".equals(status)) {
            paymentService.completePayment(subscribeId); // 결제 완료 처리
            subscribeService.updateSubscriptionStatus(subscribeId, PaymentStatus.COMPLETED); // 구독 상태 업데이트
            return ResponseEntity.ok(ApiResponse.of("결제가 완료되었습니다.")); // 한글 메시지로 변경
        } else {
            paymentService.failPayment(subscribeId); // 결제 실패 처리
            subscribeService.updateSubscriptionStatus(subscribeId, PaymentStatus.FAILED); // 구독 상태 업데이트
            return ResponseEntity.ok(ApiResponse.of("결제에 실패하였습니다.")); // 한글 메시지로 변경
        }
    }
}
