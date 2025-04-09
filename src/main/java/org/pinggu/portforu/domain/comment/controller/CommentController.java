package org.pinggu.portforu.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.common.annotation.Member;
import org.pinggu.portforu.common.dto.ApiResponse;
import org.pinggu.portforu.common.dto.AuthMember;
import org.pinggu.portforu.domain.comment.dto.request.CommentRequestDto;
import org.pinggu.portforu.domain.comment.dto.response.CommentResponseDto;
import org.pinggu.portforu.domain.comment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
//TODO 댓글 전체 조회는 어디서 이뤄지나요
//@RequestMapping("/api/v1/portfolios/{portfolioId}/comments")
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    //TODO path 추가
    @Member
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponseDto>> saveComment(
            @PathVariable("portfolioId") Long portfolioId,
            @AuthenticationPrincipal AuthMember authMember,
            @Valid @RequestBody CommentRequestDto requestDto
    ) {
        return ResponseEntity.ok().body(ApiResponse.of(
                commentService.saveComment(portfolioId, authMember.getId(), requestDto)));
    }


    //TODO CommentId는 고유값이라 portfolioId 필요없어요
    //정확하게 비교하고 싶으면 넣어도 상관없는데 맘대로 하세요
    @Member
//    @PutMapping("/{por~Id}/{commentId}") 만약 넣고싶으면 요렇게, 순서 바뀌어도 크게 상관없어요
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
            @PathVariable("portfolioId") Long portfolioId,
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal AuthMember authMember,
            @Valid @RequestBody CommentRequestDto requestDto
    ) {
        return ResponseEntity.ok().body(ApiResponse.of(
                commentService.updateComment(portfolioId, commentId, authMember.getId(), requestDto)));
    }

    @Member
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<String>> deleteComment(
            @PathVariable("portfolioId") Long portfolioId,
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal AuthMember authMember
    ) {
        commentService.deleteComment(portfolioId, commentId, authMember.getId());
        return ResponseEntity.ok(ApiResponse.of("댓글이 삭제되었습니다."));
    }
}
