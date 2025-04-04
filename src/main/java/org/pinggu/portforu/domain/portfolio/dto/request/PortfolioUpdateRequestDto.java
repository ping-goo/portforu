package org.pinggu.portforu.domain.portfolio.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PortfolioUpdateRequestDto {
    private String title;
    private String description;
    private String fileUrl;
}
