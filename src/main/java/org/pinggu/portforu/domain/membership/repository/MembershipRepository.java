package org.pinggu.portforu.domain.membership.repository;

import org.pinggu.portforu.domain.membership.entity.Membership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    @Query("SELECT m FROM Membership m WHERE m.deletedAt IS NULL")
    Page<Membership> findAllActiveMemberships(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Membership m SET "
            + "m.name = COALESCE(:name, m.name), "
            + "m.price = COALESCE(:price, m.price), "
            + "m.quantity = COALESCE(:quantity, m.quantity), "
            + "m.year = COALESCE(:year, m.year), "
            + "m.updatedAt = :now "
            + "WHERE m.id = :id AND m.deletedAt IS NULL")
    Void updateMembership(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("price") Integer price,
            @Param("quantity") Integer quantity,
            @Param("year") Integer year,
            @Param("now") Instant now
            );

}
