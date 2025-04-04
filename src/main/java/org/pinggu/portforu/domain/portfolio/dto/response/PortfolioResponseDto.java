package org.pinggu.portforu.domain.portfolio.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

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
}
