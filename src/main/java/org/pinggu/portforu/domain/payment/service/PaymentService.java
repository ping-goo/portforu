package org.pinggu.portforu.domain.payment.service;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.domain.payment.entity.Payment;
import org.pinggu.portforu.domain.payment.enums.PaymentStatus;
import org.pinggu.portforu.domain.payment.repository.PaymentRepository;
import org.pinggu.portforu.domain.subscribe.service.SubscribeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RestTemplate restTemplate;
    private final PaymentRepository paymentRepository;
    private final SubscribeService subscribeService;

    @Value("${toss.test-secret-key}")
    private String secretKey;

    public void handleSuccessPayment(String paymentKey, String orderId, Long amount) {
        try {
            String[] tokens = orderId.split("_");
            Long subscribeId = Long.parseLong(tokens[1]);

            Payment payment = paymentRepository.findBySubscribeId(subscribeId)
                    .orElseThrow(() -> new RuntimeException("결제 정보가 없습니다."));

            if (payment.getStatus() == PaymentStatus.COMPLETED) {
                throw new IllegalStateException("이미 결제가 완료된 주문입니다.");
            }

            String url = "https://api.tosspayments.com/v1/payments/confirm";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes()));

            Map<String, Object> body = new HashMap<>();
            body.put("paymentKey", paymentKey);
            body.put("orderId", orderId);
            body.put("amount", amount);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(url, request, String.class);

            payment.assignPaymentKey(paymentKey);
            payment.complete();
            paymentRepository.save(payment);
            subscribeService.updateSubscriptionStatus(subscribeId, PaymentStatus.COMPLETED);

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Toss 결제 승인 실패: " + e.getMessage());
        }
    }

    private Long parseSubscribeId(String orderId) {
        String[] tokens = orderId.split("_");
        return Long.parseLong(tokens[1]);
    }

    public void handleFailPayment(String orderId, String message) {
        try {
            Long subscribeId = parseSubscribeId(orderId);

            Payment payment = paymentRepository.findBySubscribeId(subscribeId)
                    .orElseThrow(() -> new RuntimeException("결제 정보가 없습니다."));

            payment.fail();
            paymentRepository.save(payment);
            subscribeService.updateSubscriptionStatus(subscribeId, PaymentStatus.FAILED);

            System.out.println(" 결제 실패 처리 완료 - " + message);

        } catch (Exception e) {
            System.out.println(" 결제 실패 처리 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("결제 실패 처리 중 오류 발생", e);
        }
    }

    public void cancelPayment(String orderId, String cancelReason) {
        Long subscribeId = parseSubscribeId(orderId);

        Payment payment = paymentRepository.findBySubscribeId(subscribeId)
                .orElseThrow(() -> new RuntimeException("결제 정보가 없습니다."));

        if (payment.getStatus() == PaymentStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 결제입니다.");
        }

        if (payment.getPaymentKey() == null) {
            throw new IllegalStateException("paymentKey가 저장되어 있지 않아 결제를 취소할 수 없습니다.");
        }

        String paymentKey = payment.getPaymentKey();
        String url = "https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes()));

        Map<String, Object> body = new HashMap<>();
        body.put("cancelReason", cancelReason);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(url, request, String.class);

            payment.cancel(); // 상태값 변경
            paymentRepository.save(payment);

            subscribeService.updateSubscriptionStatus(subscribeId, PaymentStatus.CANCELLED);

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("결제 취소 실패: " + e.getResponseBodyAsString(), e);
        }
    }
}
