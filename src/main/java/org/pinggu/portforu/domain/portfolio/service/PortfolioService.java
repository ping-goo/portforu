package org.pinggu.portforu.domain.portfolio.service;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.common.domain.Pagecond;
import org.pinggu.portforu.common.exception.CustomException;
import org.pinggu.portforu.domain.comment.dto.response.CommentResponseDto;
import org.pinggu.portforu.domain.comment.service.CommentService;
import org.pinggu.portforu.domain.member.entity.Member;
import org.pinggu.portforu.domain.member.repository.MemberRepository;
import org.pinggu.portforu.domain.portfolio.dto.request.PortfolioRequestDto;
import org.pinggu.portforu.domain.portfolio.dto.request.PortfolioUpdateRequestDto;
import org.pinggu.portforu.domain.portfolio.dto.response.PortfolioDetailResponseDto;
import org.pinggu.portforu.domain.portfolio.dto.response.PortfolioResponseDto;
import org.pinggu.portforu.domain.portfolio.entity.Portfolio;
import org.pinggu.portforu.domain.portfolio.repository.PortfolioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final MemberRepository memberRepository;
    private final CommentService commentService;

    @Transactional
    public PortfolioResponseDto savePortfolio(PortfolioRequestDto request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));

        Portfolio portfolio = Portfolio.builder()
                .member(member)
                .title(request.getTitle())
                .description(request.getDescription())
                .fileUrl(request.getFileUrl())
                .views(0)
                .build();

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        return PortfolioResponseDto.from(savedPortfolio);
    }

    @Transactional(readOnly = true)
    public Page<PortfolioResponseDto> findAllPortfolios(Pagecond pagecond) {
        PageRequest pageRequest = PageRequest.of(pagecond.getPageNum() - 1, pagecond.getPageSize());
        Page<Portfolio> portfolios = portfolioRepository.findAllByDeletedAtIsNull(pageRequest);

        return portfolios.map(PortfolioResponseDto::from);
    }


    @Transactional
    public PortfolioDetailResponseDto findPortfolio(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findByIdAndDeletedAtIsNull(portfolioId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."));

        portfolio.incrementViews();
        portfolioRepository.save(portfolio); //이거 영속성때문에 필요없습니다

        //코멘트 여기서 포트폴리오랑 같이 뿌려주는 것 같은데 보통 부하때문에 게시글 / 코멘트 따로 api 파서 사용합니다
        //사용자 경험에서도 차이가 많이 납니다 코멘트 문제 생긴다고 게시글까지 안불러와지면 화나요 혜원님처럼
        List<CommentResponseDto> comments = commentService.findAllComments(portfolio.getId());
        return PortfolioDetailResponseDto.from(portfolio, comments);
    }

    @Transactional
    public PortfolioResponseDto updatePortfolio(Long portfolioId, PortfolioUpdateRequestDto updateDto, Long memberId) {
        Portfolio portfolio = portfolioRepository.findByIdAndDeletedAtIsNull(portfolioId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."));

        //TODO 이런거 이렇게 해주는 방식도 좋지만
        //다른 방법으로는 위의 find 쿼리 날릴때 member도 바로 검사해버리는 방법도 있긴 합니다 근데 지금 방식이 더 좋음
        if (!portfolio.getMember().getId().equals(memberId)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "수정 권한이 없습니다.");
        }

        //TODO 문제는 안되는데 update를 따로 분리할 필요가 없습니다. 재사용 할 것도 아니고 depth만 깊어져요
        portfolio.update(updateDto.getTitle(), updateDto.getDescription(), updateDto.getFileUrl());
        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);

        return PortfolioResponseDto.from(updatedPortfolio);
    }

    @Transactional
    public Long deletePortfolio(Long portfolioId, Long memberId) {
        Portfolio portfolio = portfolioRepository.findByIdAndDeletedAtIsNull(portfolioId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."));

        if (!portfolio.getMember().getId().equals(memberId)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "수정 권한이 없습니다.");
        }

        return portfolio.delete();
    }

}