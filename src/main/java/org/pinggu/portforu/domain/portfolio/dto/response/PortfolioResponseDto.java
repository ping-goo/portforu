package org.pinggu.portforu.domain.portfolio.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.pinggu.portforu.domain.portfolio.entity.Portfolio;

import java.time.LocalDateTime;

@Getter
@Builder
public class PortfolioResponseDto {

    private Long id;
    private Long memberId;
    private String title;
    private String description;
    private String fileUrl;
    private Integer views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static PortfolioResponseDto from(Portfolio portfolio){
        return PortfolioResponseDto.builder()
                .id(portfolio.getId())
                .memberId(portfolio.getMember().getId())
                .title(portfolio.getTitle())
                .description(portfolio.getDescription())
                .fileUrl(portfolio.getFileUrl())
                .views(portfolio.getViews())
                .createdAt(portfolio.getCreatedAt())
                .updatedAt(portfolio.getUpdatedAt())
                .deletedAt(portfolio.getDeletedAt())
                .build();
    }

}
