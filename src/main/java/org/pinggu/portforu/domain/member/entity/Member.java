package org.pinggu.portforu.domain.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pinggu.portforu.common.domain.BaseEntity;
import org.pinggu.portforu.domain.member.enums.UserRole;
import org.pinggu.portforu.domain.portfolio.entity.Portfolio;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "member" , cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Portfolio> portfolios = new ArrayList<>();

    public Member(String email, String password, String name, String phoneNumber, String address, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.userRole = userRole;
    }

}