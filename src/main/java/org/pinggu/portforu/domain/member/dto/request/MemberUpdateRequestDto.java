package org.pinggu.portforu.domain.member.dto.request;

import lombok.Getter;

@Getter
public class MemberUpdateRequestDto {

    private String oldPassword;
    private String newPassword;
    private String name;
    private String phoneNumber;
    private String address;

    public MemberUpdateRequestDto(String oldPassword, String newPassword, String name, String phoneNumber, String address) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
  
}