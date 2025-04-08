package org.pinggu.portforu.domain.subscribe.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pinggu.portforu.common.domain.BaseEntity;
import org.pinggu.portforu.domain.member.entity.Member;
import org.pinggu.portforu.domain.membership.entity.Membership;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "subscribes")
public class Subscribe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 사용자 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id", nullable = false)
    private Membership membership; // 멤버십 ID

    @Column(nullable = false)
    private LocalDateTime startDate; // 구독 시작일

    @Column(nullable = false)
    private LocalDateTime endDate; // 구독 종료일

    // 생성자
    @Builder
    public Subscribe(Member member, Membership membership, LocalDateTime startDate, LocalDateTime endDate) {
        this.member = member;
        this.membership = membership;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Subscribe update(Membership membership, LocalDateTime startDate, LocalDateTime endDate) {
        return Subscribe.builder()
                .member(this.member)
                .membership(membership)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public boolean isActive() {
        return LocalDateTime.now().isBefore(this.endDate);
    }
}