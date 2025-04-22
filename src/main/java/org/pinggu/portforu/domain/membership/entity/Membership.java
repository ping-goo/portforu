package org.pinggu.portforu.domain.membership.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.pinggu.portforu.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "memberships")
@SQLDelete(sql = "UPDATE memberships SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Membership extends BaseEntity {

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer year;

    @Builder
    public Membership(String name, Integer price, Integer quantity, Integer year) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.year = year;
    }

    public void update(String name, Integer price, Integer quantity, Integer year) {
        if (name != null) this.name = name;
        if (price != null) this.price = price;
        if (quantity != null) this.quantity = quantity;
        if (year != null) this.year = year;
    }

    // 정원 감소
    public void decreaseQuantity() {
        if (this.quantity <= 0) {
            throw new IllegalStateException("멤버십 정원이 가득 찼습니다.");
        }
        this.quantity--;
    }

    // 정원 증가
    public void increaseQuantity() {
        this.quantity++;
    }

}
