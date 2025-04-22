package org.pinggu.portforu.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.common.annotation.Member;
import org.pinggu.portforu.common.dto.ApiResponse;
import org.pinggu.portforu.common.dto.AuthMember;
import org.pinggu.portforu.domain.member.dto.request.MemberDeleteRequestDto;
import org.pinggu.portforu.domain.member.dto.request.PasswordUpdateRequestDto;
import org.pinggu.portforu.domain.member.dto.response.MemberResponseDto;
import org.pinggu.portforu.domain.member.dto.request.MemberUpdateRequestDto;
import org.pinggu.portforu.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @Member
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponseDto>> findMember(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(ApiResponse.of(memberService.findMember(authMember, id)));
    }

    @Member
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateMember(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long id,
            @Valid @RequestBody MemberUpdateRequestDto requestDto
    ) {
        memberService.updateMember(authMember, id, requestDto);

        return ResponseEntity.ok(ApiResponse.of("수정완료"));
    }

    @Member
    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse<String>> updatePassword(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long id,
            @Valid @RequestBody PasswordUpdateRequestDto requestDto
    ) {
        memberService.updatePassword(authMember, id, requestDto);

        return ResponseEntity.ok(ApiResponse.of("수정 완료"));
    }

    @Member
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteMember(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long id,
            @Valid @RequestBody MemberDeleteRequestDto requestDto
    ) {
        return ResponseEntity.ok(ApiResponse.of(memberService.deleteMember(authMember, id, requestDto)));
    }

}