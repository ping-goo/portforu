package org.pinggu.portforu.domain.portfolio.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioRequestDto {

    private String title;
    private String description;
    private String fileUrl;
}
