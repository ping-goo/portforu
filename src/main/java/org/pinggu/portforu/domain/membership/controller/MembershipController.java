package org.pinggu.portforu.domain.membership.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.common.dto.ApiResponse;
import org.pinggu.portforu.common.dto.AuthMember;
import org.pinggu.portforu.domain.membership.dto.request.CreateMembershipRequestDto;
import org.pinggu.portforu.domain.membership.dto.request.UpdateMembershipRequestDto;
import org.pinggu.portforu.domain.membership.dto.response.MembershipResponseDto;
import org.pinggu.portforu.domain.membership.service.MembershipService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/memberships")
public class MembershipController {
    private final MembershipService membershipService;

    @PostMapping
    public ResponseEntity<ApiResponse<MembershipResponseDto>> saveMembership(
            @AuthenticationPrincipal AuthMember authMember,
            @Valid @RequestBody CreateMembershipRequestDto request
    ) {
        MembershipResponseDto response = membershipService.saveMembership(authMember, request);
        return ResponseEntity.ok().body(ApiResponse.of(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MembershipResponseDto>>> findByAllMemberships(
            @AuthenticationPrincipal AuthMember authMember,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<MembershipResponseDto> memberships = membershipService.findByAllMemberships(authMember, pageable);
        return ResponseEntity.ok().body(ApiResponse.of(memberships));
    }

    @GetMapping("/{membershipId}")
    public ResponseEntity<ApiResponse<MembershipResponseDto>> findByIdMemberships(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long membershipId
    ) {
        MembershipResponseDto response = membershipService.findByIdMemberships(authMember, membershipId);
        return ResponseEntity.ok().body(ApiResponse.of(response));
    }

    @PutMapping("/{membershipId}")
    public ResponseEntity<ApiResponse<MembershipResponseDto>> updateMembership(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long membershipId,
            @RequestBody UpdateMembershipRequestDto request
    ) {
        MembershipResponseDto updatedMembership = membershipService.updateMembership(authMember, membershipId, request);
        return ResponseEntity.ok(ApiResponse.of(updatedMembership));
    }

    @DeleteMapping("/{membershipId}")
    public ResponseEntity<ApiResponse<MembershipResponseDto>> deleteMembership(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long membershipId
    ) {
        MembershipResponseDto response = membershipService.deleteMembership(authMember, membershipId);
        return ResponseEntity.ok().body(ApiResponse.of(response));
    }
}
