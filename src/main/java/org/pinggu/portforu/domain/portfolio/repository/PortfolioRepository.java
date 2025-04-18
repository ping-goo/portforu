package org.pinggu.portforu.domain.portfolio.repository;

import org.pinggu.portforu.domain.portfolio.entity.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    @EntityGraph(attributePaths = "member")
    Optional<Portfolio> findWithMemberById(Long id);

    Page<Portfolio> findAllByDeletedAtIsNull(Pageable pageable);

    Page<Portfolio> findAllByMemberIdAndDeletedAtIsNull(Long memberId, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Portfolio p " +
            "SET p.title = COALESCE(:title, p.title), " +
            "    p.description = COALESCE(:description, p.description), " +
            "    p.fileUrl = COALESCE(:fileUrl, p.fileUrl), " +
            "    p.updatedAt = :now " +
            "WHERE p.id = :portfolioId AND p.deletedAt IS NULL")
    Void updatePortfolio(@Param("portfolioId") Long portfolioId,
                            @Param("title") String title,
                            @Param("description") String description,
                            @Param("fileUrl") String fileUrl,
                            @Param("now") Instant now);

}
