package org.pinggu.portforu.domain.subscribe.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.pinggu.portforu.domain.payment.entity.Payment;
import org.pinggu.portforu.domain.subscribe.entity.Subscribe;

import java.time.LocalDateTime;

@Getter
@Builder
public class SubscribeResponseDto {
    private final Long id; // 구독 ID
    private final Long memberId; // 사용자 ID
    private final Long membershipId; // 멤버십 ID
    private final String paymentMethod; // 결제 수단
    private final LocalDateTime startDate; // 구독 시작일
    private final LocalDateTime endDate; // 구독 종료일
    private final boolean active;

    public static SubscribeResponseDto from(Subscribe subscribe, Payment payment) {
        return SubscribeResponseDto.builder()
                .id(subscribe.getId())
                .memberId(subscribe.getMember().getId())
                .membershipId(subscribe.getMembership().getId())
                .paymentMethod(payment.getPaymentMethod().name())
                .startDate(subscribe.getStartDate())
                .endDate(subscribe.getEndDate())
                .active(subscribe.isActive())
                .build();
    }
}
