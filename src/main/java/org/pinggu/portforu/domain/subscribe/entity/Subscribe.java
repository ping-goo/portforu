package org.pinggu.portforu.domain.subscribe.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pinggu.portforu.common.domain.BaseEntity;
import org.pinggu.portforu.domain.member.entity.Member;
import org.pinggu.portforu.domain.membership.entity.Membership;
import org.pinggu.portforu.domain.subscribe.enums.SubscribeStatus;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "subscribes")
public class Subscribe extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id", nullable = false)
    private Membership membership;

    @Column(nullable = false)
    private Instant startDate;

    @Column(nullable = false)
    private Instant endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscribeStatus status = SubscribeStatus.PENDING;

    @Builder
    public Subscribe(Member member, Membership membership, Instant startDate, Instant endDate) {
        this.member = member;
        this.membership = membership;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isActive() {
        return Instant.now().isBefore(this.endDate);
    }

    public void cancel() {
        if (this.status == SubscribeStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 구독입니다.");
        }
        if (this.status != SubscribeStatus.ACTIVE) {    //TODO 실무 환경에선 실제 서드파티에선 결제했으나 서버/DB애 반영이 늦을 수가 있음,
            // 특히 구글은 그런일 빈번함 구글 플레이스토어에 인앱 구독 앱이 많지 않은 이유 중 하나임
            //이러한 것들을 고려하면 ACTIVE만 보는건 상당히 보수적일수가 있음. PENDING일 떄도 허용하거나 뭐 등등.. 정책적으로 잘 정리하길 바람
            throw new IllegalStateException("구독이 활성 상태일 때만 취소할 수 있습니다.");
        }
        this.status = SubscribeStatus.CANCELLED;
    }

    public void expire() {
        if (this.status == SubscribeStatus.EXPIRED) {
            return;
        }
        this.status = SubscribeStatus.EXPIRED;
    }

    public void activate() {
        if (this.status != SubscribeStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태에서만 ACTIVE로 전환할 수 있습니다.");
        }
        this.status = SubscribeStatus.ACTIVE;
    }

    public void fail() {
        if (this.status == SubscribeStatus.FAILED) {
            return;
        }
        this.status = SubscribeStatus.FAILED;
    }
}
