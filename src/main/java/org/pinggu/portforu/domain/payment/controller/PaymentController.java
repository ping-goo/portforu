package org.pinggu.portforu.domain.payment.controller;

import lombok.RequiredArgsConstructor;

import org.pinggu.portforu.common.dto.ApiResponse;
import org.pinggu.portforu.domain.payment.entity.Payment;
import org.pinggu.portforu.domain.payment.enums.PaymentStatus;
import org.pinggu.portforu.domain.payment.repository.PaymentRepository;
import org.pinggu.portforu.domain.subscribe.service.SubscribeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final RestTemplate restTemplate;
    private final PaymentRepository paymentRepository;
    private final SubscribeService subscribeService;

    @Value("${toss.test-secret-key}")
    private String secretKey;

    @GetMapping("/success")
    public ResponseEntity<ApiResponse<String>> confirmPayment(
            @RequestParam String paymentKey,
            @RequestParam String orderId,   // subscribeId로 사용
            @RequestParam Long amount
    ) {
        String url = "https://api.tosspayments.com/v1/payments/confirm";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes()));

        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", paymentKey);
        body.put("orderId", orderId);
        body.put("amount", amount);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            // 1. Toss에 결제 승인 요청
            restTemplate.postForEntity(url, request, String.class);

            // 2. subscribeId 추출
            Long subscribeId = Long.parseLong(orderId);

            // 3. Payment 상태 업데이트
            Payment payment = paymentRepository.findBySubscribeId(subscribeId)
                    .orElseThrow(() -> new RuntimeException("결제 정보가 없습니다."));

            payment.complete();
            paymentRepository.save(payment);

            // 4. 구독 상태 처리
            subscribeService.updateSubscriptionStatus(subscribeId, PaymentStatus.COMPLETED);

            return ResponseEntity.ok(ApiResponse.of("결제가 완료되었습니다."));

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(ApiResponse.of("결제 승인 실패: " + e.getResponseBodyAsString()));
        }
    }

    @GetMapping("/fail")
    public ResponseEntity<ApiResponse<String>> handlePaymentFail(
            @RequestParam String code,
            @RequestParam String message,
            @RequestParam String orderId
    ) {
        try {
            Long subscribeId = Long.parseLong(orderId);

            Payment payment = paymentRepository.findBySubscribeId(subscribeId)
                    .orElseThrow(() -> new RuntimeException("결제 정보가 없습니다."));

            payment.fail();
            paymentRepository.save(payment);

            subscribeService.updateSubscriptionStatus(subscribeId, PaymentStatus.FAILED);

            return ResponseEntity.ok(ApiResponse.of("결제가 실패했습니다. 사유: " + message + " (코드: " + code + ")"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.of("결제 실패 처리 중 오류가 발생했습니다."));
        }
    }

}
