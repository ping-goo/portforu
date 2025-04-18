package org.pinggu.portforu.domain.portfolio.service;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.common.exception.CustomException;
import org.pinggu.portforu.domain.portfolio.entity.Portfolio;
import org.pinggu.portforu.domain.portfolio.repository.PortfolioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PortfolioFinder {

    private final PortfolioRepository portfolioRepository;

    public Portfolio findPortfolioById(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."));

        if (portfolio.isDeleted()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 삭제된 게시물입니다.");
        }

        return portfolio;
    }

}
