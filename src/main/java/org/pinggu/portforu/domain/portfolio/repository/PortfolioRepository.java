package org.pinggu.portforu.domain.portfolio.repository;

import org.pinggu.portforu.domain.portfolio.entity.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Page<Portfolio> findAllByMember(Long memberId, Pageable pageable);

}
