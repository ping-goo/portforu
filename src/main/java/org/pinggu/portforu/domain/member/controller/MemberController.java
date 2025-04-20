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
@RequestMapping("/api/v1/members/{id}") //TODO 이거 {id}를 왜 여기서 가지고 있는지, 위험한 방식임, 유연성이 낮음
public class MemberController {

    private final MemberService memberService;

    //TODO 각 메소드에 Valid어노테이션 빠져있음

    @Member
    @GetMapping
    public ResponseEntity<ApiResponse<MemberResponseDto>> findMember(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(ApiResponse.of(memberService.findMember(authMember, id)));
    }

    @Member
    @PutMapping
    public ResponseEntity<ApiResponse<MemberResponseDto>> updateMember(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long id,
            @Valid @RequestBody MemberUpdateRequestDto requestDto
    ) {
        return ResponseEntity.ok().body(ApiResponse.of(memberService.updateMember(authMember, id, requestDto)));
    }

    @Member
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<MemberResponseDto>> updatePassword(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long id,
            @Valid @RequestBody PasswordUpdateRequestDto requestDto
    ) {
        return ResponseEntity.ok().body(ApiResponse.of(memberService.updatePassword(authMember, id, requestDto)));
    }

    @Member
    @DeleteMapping
    public ResponseEntity<ApiResponse<Long>> deleteMember(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long id,
            @Valid @RequestBody MemberDeleteRequestDto requestDto
    ) {
        return ResponseEntity.ok(ApiResponse.of(memberService.deleteMember(authMember, id, requestDto)));
    }

}