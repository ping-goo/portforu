package org.pinggu.portforu.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // 결제 수단

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // 결제 상태

    public Payment(PaymentMethod paymentMethod, PaymentStatus status) {
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, PAYPAL, OTHER
    }

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED
    }
}

