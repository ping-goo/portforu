package org.pinggu.portforu.domain.membership.dto.request;

import lombok.Getter;

@Getter
public class CreateMembershipRequestDto {
    private String name;
    private Integer price;
    private Integer quantity;
    private Integer year;
}
