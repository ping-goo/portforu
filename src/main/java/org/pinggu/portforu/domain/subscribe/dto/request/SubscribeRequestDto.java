package org.pinggu.portforu.domain.subscribe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SubscribeRequestDto {
    private final PaymentMethod paymentMethod; // 결제 수단
}
