package org.pinggu.portforu.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pinggu.portforu.domain.payment.enums.PaymentMethod;
import org.pinggu.portforu.domain.payment.enums.PaymentStatus;
import org.pinggu.portforu.domain.subscribe.entity.Subscribe;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscribe_id", nullable = false)
    private Subscribe subscribe;

    @Builder
    public Payment(PaymentMethod paymentMethod, PaymentStatus status, Subscribe subscribe) {
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.subscribe = subscribe;
    }

    // 결제 완료 처리
    public void complete() {
        this.status = PaymentStatus.COMPLETED;
    }

    // 결제 실패 처리
    public void fail() {
        this.status = PaymentStatus.FAILED;
    }

    // 결제 만료 처리
    public void expire() {
        this.status = PaymentStatus.EXPIRED;
    }
}
