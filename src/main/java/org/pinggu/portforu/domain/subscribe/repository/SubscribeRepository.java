package org.pinggu.portforu.domain.subscribe.repository;

import org.pinggu.portforu.domain.member.entity.Member;
import org.pinggu.portforu.domain.subscribe.entity.Subscribe;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    List<Subscribe> findAllByMemberAndDeletedAtIsNull(Member member);

    @Query("""
        SELECT COUNT(s) > 0 FROM Subscribe s
        JOIN Payment p ON p.subscribe = s
        WHERE s.member.id = :memberId
        AND s.membership.id = :membershipId
        AND s.status IN ('ACTIVE', 'CANCELLED')
        AND p.status = 'COMPLETED'
        AND s.deletedAt IS NULL
        """)
    boolean hasValidSubscription(@Param("memberId") Long memberId,
                                 @Param("membershipId") Long membershipId);

    @EntityGraph(attributePaths = "membership")
    List<Subscribe> findAllByEndDateBeforeAndDeletedAtIsNull(Instant time);

}