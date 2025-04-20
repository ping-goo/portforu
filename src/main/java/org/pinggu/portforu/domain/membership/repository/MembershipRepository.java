package org.pinggu.portforu.domain.membership.repository;

import org.pinggu.portforu.domain.membership.entity.Membership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    //TODO JPQL 이용할 필요가 없음 JPA로만 해도 가능
    @Query("SELECT m FROM Membership m WHERE m.deletedAt IS NULL")
    Page<Membership> findAllActiveMemberships(Pageable pageable);

    Optional<Membership> findByIdAndDeletedAtIsNull(Long membershipId);

    //TODO 이런건 제발 안해줬으면 함 / 엔티티 필드 변 경-> 저장 및 업데이트 방식으로 가야함어차피 한 데이터 추가하는거라
    //이 방식은 캐시된 엔티티 불일치 가능성이 생기고, updatedAt과 같은 공통필드가 자동 반영 안될수가있음, Auditing때 꼬이는 일도 많음
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Membership m SET "
            + "m.name = COALESCE(:name, m.name), "
            + "m.price = COALESCE(:price, m.price), "
            + "m.quantity = COALESCE(:quantity, m.quantity), "
            + "m.year = COALESCE(:year, m.year) "
            + "WHERE m.id = :id AND m.deletedAt IS NULL")
    Integer updateMembership(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("price") Integer price,
            @Param("quantity") Integer quantity,
            @Param("year") Integer year
    );

}
