package org.pinggu.portforu.domain.subscribe.repository;

import org.pinggu.portforu.domain.member.entity.Member;
import org.pinggu.portforu.domain.membership.entity.Membership;
import org.pinggu.portforu.domain.membership.entity.Membership;
import org.pinggu.portforu.domain.payment.enums.PaymentStatus;
import org.pinggu.portforu.domain.subscribe.entity.Subscribe;
import org.pinggu.portforu.domain.subscribe.enums.SubscribeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    List<Subscribe> findAllByMemberAndIsDeletedFalse(Member member);

    @Query("""
        SELECT COUNT(s) > 0 FROM Subscribe s
        JOIN Payment p ON p.subscribe = s
        WHERE s.member.id = :memberId
        AND s.membership.id = :membershipId
        AND s.status IN ('ACTIVE', 'CANCELLED')
        AND p.status = 'COMPLETED'
        AND s.isDeleted IS NULL
        """)
    boolean hasValidSubscription(@Param("memberId") Long memberId,
                                 @Param("membershipId") Long membershipId);

    @Modifying
    @Query("UPDATE Subscribe s SET s.status = :toStatus " +
            "WHERE s.status = :fromStatus AND s.endDate < :now AND s.isDeleted = false")
    int bulkExpireSubscriptions(@Param("fromStatus") SubscribeStatus fromStatus,
                                @Param("toStatus") SubscribeStatus toStatus,
                                @Param("now") Instant now);


}