package org.pinggu.portforu.domain.membership.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MembershipResponseDto {
    private final Long id;
    private final String name;
    private final Integer price;
    private final Integer quantity;
    private final Integer year;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;

    public MembershipResponseDto(Long id, String name, Integer price, Integer quantity, Integer year, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.year = year;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
}
