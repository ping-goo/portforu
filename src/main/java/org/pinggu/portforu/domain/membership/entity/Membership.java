package org.pinggu.portforu.domain.membership.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "memberships")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 멤버십 이름
    private int price; // 가격

    public Membership(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
