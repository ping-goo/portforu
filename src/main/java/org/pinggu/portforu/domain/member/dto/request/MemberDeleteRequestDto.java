package org.pinggu.portforu.domain.member.dto.request;

import lombok.Getter;

@Getter
public class MemberDeleteRequestDto {

    private String password;
    private String passwordConfirm;

    public MemberDeleteRequestDto(String password, String passwordConfirm) {
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

}
