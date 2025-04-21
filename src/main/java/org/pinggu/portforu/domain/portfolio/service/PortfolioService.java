package org.pinggu.portforu.domain.portfolio.service;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.common.domain.Pagecond;
import org.pinggu.portforu.common.dto.AuthMember;
import org.pinggu.portforu.common.exception.CustomException;
import org.pinggu.portforu.domain.member.entity.Member;
import org.pinggu.portforu.domain.member.service.MemberFinder;
import org.pinggu.portforu.domain.portfolio.dto.request.PortfolioRequestDto;
import org.pinggu.portforu.domain.portfolio.dto.request.PortfolioUpdateRequestDto;
import org.pinggu.portforu.domain.portfolio.dto.response.PortfolioListResponseDto;
import org.pinggu.portforu.domain.portfolio.dto.response.PortfolioResponseDto;
import org.pinggu.portforu.domain.portfolio.entity.Portfolio;
import org.pinggu.portforu.domain.portfolio.repository.PortfolioRepository;
import org.pinggu.portforu.domain.subscribe.validator.SubscribeValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioFinder portfolioFinder;
    private final MemberFinder memberFinder;
    private final SubscribeValidator subscribeValidator;
    private final PortfolioRepository portfolioRepository;

    @Transactional
    public PortfolioResponseDto savePortfolio(AuthMember authMember, PortfolioRequestDto requestDto) {
        Member member = Member.fromAuthMember(authMember);

        Portfolio portfolio = Portfolio.builder()
                .member(member)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .fileUrl(requestDto.getPortfolioFileUrl())
                .views(0)
                .build();

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        return PortfolioResponseDto.from(savedPortfolio);
    }

    @Transactional(readOnly = true)
    public Page<PortfolioListResponseDto> findAllPortfolios(Pagecond pagecond) {
        Pageable pageable = PageRequest.of(pagecond.getPageNum() - 1, pagecond.getPageSize());
        Page<Portfolio> portfolioPage = portfolioRepository.findAllByDeletedAtIsNull(pageable);

        return portfolioPage.map(PortfolioListResponseDto::from);
    }

    @Transactional(readOnly = true)
    public PortfolioResponseDto findPortfolio(AuthMember authMember, Long portfolioId) {
        Portfolio portfolio = portfolioFinder.findPortfolioById(portfolioId);

        if (!portfolio.getMember().getId().equals(authMember.getId())) {
            if (!subscribeValidator.isSubscribed(authMember.getId())) {
                Member viewer = Member.fromAuthMember(authMember);

                if (viewer.getViewCount() <= 0) {
                    throw new CustomException(HttpStatus.FORBIDDEN, "포트폴리오 조회 가능 횟수를 모두 사용했습니다.");
                }

                viewer.decrementRemainingViewCount();
            }
        }

        portfolio.incrementViews();

        return PortfolioResponseDto.from(portfolio);
    }

    @Transactional(readOnly = true)
    public Page<PortfolioListResponseDto> findMyAllPortfolios(AuthMember authMember, Long memberId, Pagecond pagecond) {
        memberFinder.validateOwnership(authMember, memberId);

        Pageable pageable = PageRequest.of(pagecond.getPageNum() - 1, pagecond.getPageSize());
        Page<Portfolio> portfolios = portfolioRepository.findAllByMemberIdAndDeletedAtIsNull(memberId, pageable);

        return portfolios.map(PortfolioListResponseDto::from);
    }

    @Transactional(readOnly = true)
    public PortfolioResponseDto findMyPortfolioDetail(AuthMember authMember, Long memberId, Long portfolioId) {
        memberFinder.validateOwnership(authMember, memberId);
        Portfolio portfolio = portfolioFinder.findPortfolioById(portfolioId);

        if (!portfolio.getMember().getId().equals(memberId)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "게시물에 대한 접근 권한이 없습니다.");
        }

        return PortfolioResponseDto.from(portfolio);
    }

    @Transactional
    public PortfolioResponseDto updatePortfolio(AuthMember authMember, Long portfolioId, PortfolioUpdateRequestDto requestDto) {
        Portfolio portfolio = portfolioFinder.findPortfolioById(portfolioId);

        if (!portfolio.getMember().getId().equals(authMember.getId())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "수정 권한이 없습니다.");
        }

        String newFileUrl = requestDto.getPortfolioFileUrl() != null
                ? requestDto.getPortfolioFileUrl()
                : portfolio.getFileUrl();

        Instant now = Instant.now();
        portfolioRepository.updatePortfolio(
                portfolioId, requestDto.getTitle(), requestDto.getDescription(), newFileUrl, now
        );

        Portfolio updatedPortfolio = portfolioFinder.findPortfolioById(portfolioId);
        return PortfolioResponseDto.from(updatedPortfolio);
    }

    @Transactional
    public Long deletePortfolio(AuthMember authMember, Long portfolioId) {
        Portfolio portfolio = portfolioFinder.findPortfolioById(portfolioId);

        if (!portfolio.getMember().getId().equals(authMember.getId())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "삭제 권한이 없습니다.");
        }

        return portfolio.delete();
    }

}