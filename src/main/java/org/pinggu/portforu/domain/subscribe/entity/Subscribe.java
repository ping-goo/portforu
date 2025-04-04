package org.pinggu.portforu.domain.subscribe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pinggu.portforu.common.domain.BaseEntity;
import org.pinggu.portforu.domain.payment.entity.Payment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "subscribes")
public class Subscribe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId; // 사용자 ID

    @Column(nullable = false)
    private Long membershipId; // 멤버십 ID

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment; // 결제 entity

    @Column(nullable = false)
    private LocalDateTime startDate; // 구독 시작일

    @Column(nullable = false)
    private LocalDateTime endDate; // 구독 종료일

    // 생성자
    public Subscribe(Long memberId, Long membershipId, Payment payment, LocalDateTime startDate, LocalDateTime endDate) {
        this.memberId = memberId;
        this.membershipId = membershipId;
        this.payment = payment;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // 업데이트할시 객체의 불변성을 위해 만든 메서드(기존 memberId는 그대로 유지하면서 나머지 필드를 새 값으로 설정)
    public Subscribe update(Long membershipId, Payment payment, LocalDateTime startDate, LocalDateTime endDate) {
        return new Subscribe(this.memberId, membershipId, payment, startDate, endDate);
    }
}