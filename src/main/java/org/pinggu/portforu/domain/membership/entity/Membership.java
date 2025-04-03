package org.pinggu.portforu.domain.membership.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pinggu.portforu.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "memberships")
public class Membership extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id")
    private Long id;
    private String name;
    private Integer price;
    private Integer quantity;
    private Integer year;

    @Builder
    public Membership(String name, Integer price, Integer quantity, Integer year) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.year = year;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
