package org.pinggu.portforu.domain.portfolio.service;

import org.pinggu.portforu.common.domain.Pagecond;
import org.pinggu.portforu.domain.portfolio.dto.request.PortfolioRequestDto;
import org.pinggu.portforu.domain.portfolio.dto.request.PortfolioUpdateRequestDto;
import org.pinggu.portforu.domain.portfolio.dto.response.PortfolioResponseDto;
import org.springframework.data.domain.Page;

public interface PortfolioService {
    PortfolioResponseDto createPortfolio(PortfolioRequestDto request, Long memberId);
    Page<PortfolioResponseDto> getPortfolios(Pagecond page);
    PortfolioResponseDto getPortfolio(Long portfolioId);
    PortfolioResponseDto updatePortfolio(Long portfolioId, PortfolioUpdateRequestDto updateDto,Long memberId);
    void deletePortfolio(Long portfolioId,Long memberId);
}
