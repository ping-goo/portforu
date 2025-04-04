package org.pinggu.portforu.domain.portfolio.controller;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.common.domain.PageInfo;
import org.pinggu.portforu.common.domain.Pagecond;
import org.pinggu.portforu.common.dto.ApiResponse;
import org.pinggu.portforu.common.dto.AuthMember;
import org.pinggu.portforu.domain.portfolio.dto.request.PortfolioRequestDto;
import org.pinggu.portforu.domain.portfolio.dto.request.PortfolioUpdateRequestDto;
import org.pinggu.portforu.domain.portfolio.dto.response.PortfolioResponseDto;
import org.pinggu.portforu.domain.portfolio.service.PortfolioService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Member
    @PostMapping
    public ResponseEntity<ApiResponse<PortfolioResponseDto>> createPortfolio(
            @AuthenticationPrincipal AuthMember authMember,
            @RequestBody PortfolioRequestDto requestDto
            ){

        PortfolioResponseDto responseDto = portfolioService.createPortfolio(requestDto,authMember.getId());
        return ResponseEntity.ok(ApiResponse.of(responseDto));
    }

    @Member
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getPortfolios(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize
    ){

        Pagecond pagecond = new Pagecond(pageNum, pageSize);
        Page<PortfolioResponseDto> portfolios = portfolioService.getPortfolios(pagecond);

        PageInfo pageInfo = PageInfo.builder()
                .pageNum(pagecond.getPageNum())
                .pageSize(pagecond.getPageSize())
                .totalElement(portfolios.getTotalElements())
                .totalPage(portfolios.getTotalPages())
                .build();

        return ResponseEntity.ok(ApiResponse.of(portfolios.getContent(), pageInfo));
    }

    @Member
    @GetMapping("/{portfolioId}")
    public ResponseEntity<ApiResponse<PortfolioResponseDto>> getPortfolio(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long portfolioId,
            @RequestBody PortfolioRequestDto requestDto){

        PortfolioResponseDto responseDto = portfolioService.getPortfolio(portfolioId);
        return ResponseEntity.ok(ApiResponse.of(responseDto));
    }

    @Member
    @PutMapping("/{portfolioId}")
    public ResponseEntity<ApiResponse<PortfolioResponseDto>> updatePortfolio(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long portfolioId,
            @RequestBody PortfolioUpdateRequestDto requestDto
    ){

        PortfolioResponseDto responseDto = portfolioService.updatePortfolio(portfolioId,requestDto,authMember.getId());
        return ResponseEntity.ok(ApiResponse.of(responseDto));
    }

    @Member
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deletePortfolio(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long portfolioId
    ){

        portfolioService.deletePortfolio(portfolioId,authMember.getId());
        return ResponseEntity.ok(ApiResponse.of(null));
    }
}
