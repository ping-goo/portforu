package org.pinggu.portforu.domain.membership.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MembershipSaveRequestDto {
    @NotNull(message = "멤버쉽 이름은 필수입니다.")
    private String name;
    @NotNull(message = "가격은 필수입니다.")
    private Integer price;
    @NotNull(message = "수량은 필수입니다.")
    private Integer quantity;
    @NotNull(message = "사용년도는 필수입니다.")
    private Integer year;
}
