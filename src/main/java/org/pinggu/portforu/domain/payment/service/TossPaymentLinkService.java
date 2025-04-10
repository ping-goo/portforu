package org.pinggu.portforu.domain.payment.service;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.common.exception.CustomException;
import org.pinggu.portforu.domain.subscribe.entity.Subscribe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TossPaymentLinkService {

    @Value("${toss.test-secret-key}")
    private String tossSecretKey;

    private final RestTemplate restTemplate;

    public String savePaymentLink(Subscribe subscribe) {

        if (subscribe.getMembership() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "유효한 멤버십 정보가 없습니다.");
        }

        String orderId = "subscribe_" + subscribe.getId();  // 고유 주문 ID

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("amount", subscribe.getMembership().getPrice());  // 금액
        requestBody.put("orderId", orderId);  // 주문 ID
        requestBody.put("orderName", subscribe.getMembership().getName());  // 상품명
        requestBody.put("successUrl", "http://localhost:8080/api/v1/toss-payments/success");  // 결제 성공 URL
        requestBody.put("failUrl", "http://localhost:8080/api/v1/toss-payments/fail");  // 결제 실패 URL

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(tossSecretKey, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.tosspayments.com/v1/payments",
                HttpMethod.POST,
                request,
                Map.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().get("paymentLinkUrl").toString();  // 결제 링크 반환
        } else {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "결제링크 생성 실패");
        }
    }
}