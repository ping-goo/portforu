package org.pinggu.portforu.domain.subscribe.controller;

import lombok.RequiredArgsConstructor;

import org.pinggu.portforu.common.domain.PageInfo;
import org.pinggu.portforu.common.dto.ApiResponse;
import org.pinggu.portforu.common.dto.AuthMember;
import org.pinggu.portforu.domain.subscribe.dto.request.SubscribeRequestDto;
import org.pinggu.portforu.domain.subscribe.dto.response.SubscribeResponseDto;
import org.pinggu.portforu.domain.subscribe.service.SubscribeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SubscribeController {

    private final SubscribeService subscribeService;

    // 구독 생성
    @PostMapping("/memberships/{membershipId}/subscribes")
    public ResponseEntity<ApiResponse<SubscribeResponseDto>> createSubscribe(
            @PathVariable Long membershipId,
            @RequestBody SubscribeRequestDto requestDto,
            @AuthenticationPrincipal AuthMember member) {
        // PathVariable의 membershipId를 사용하고, 나머지 값은 요청 DTO에서 사용합니다.
        Long memberId = member.getId();
        SubscribeResponseDto dto = subscribeService.createSubscribe(memberId, membershipId, requestDto);
        return ResponseEntity.ok(ApiResponse.of(dto));
    }

    // 구독 목록 조회 (페이징 적용)
    @GetMapping("/my/subscribes")
    public ResponseEntity<ApiResponse<List<SubscribeResponseDto>>> getAllSubscribes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal AuthMember member) {

        Long memberId = member.getId();
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<SubscribeResponseDto> subscribePage = subscribeService.getAllSubscribes(pageable, memberId);

        PageInfo pageInfo = PageInfo.builder()
                .pageNum(page)
                .pageSize(size)
                .totalElement(subscribePage.getTotalElements())
                .totalPage(subscribePage.getTotalPages())
                .build();

        return ResponseEntity.ok(ApiResponse.of(subscribePage.getContent(), pageInfo));
    }

    // 구독 취소
    @DeleteMapping("/my/subscribes/{subscribeId}")
    public ResponseEntity<ApiResponse<String>> deleteSubscribe(@PathVariable Long subscribeId) {
        subscribeService.deleteSubscribe(subscribeId);
        return ResponseEntity.ok(ApiResponse.of("구독이 취소되었습니다."));
    }
}
