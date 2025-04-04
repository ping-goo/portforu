package org.pinggu.portforu.domain.membership.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateMembershipRequestDto {
    private String name;
    private Integer price;
    private Integer quantity;
    private Integer year;
}
