//package org.pinggu.portforu.domain.comment.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.pinggu.portforu.common.dto.ApiResponse;
//import org.pinggu.portforu.common.dto.AuthMember;
//import org.pinggu.portforu.domain.comment.dto.request.CommentRequestDto;
//import org.pinggu.portforu.domain.comment.dto.response.CommentResponseDto;
//import org.pinggu.portforu.domain.comment.service.CommentService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/portfolios/{portfolioId}/comments")
//public class CommentController {
//
//    private final CommentService commentService;
//
//    @Member
//    @PostMapping
//    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
//            @PathVariable("portfolioId") Long portfolioId,
//            @AuthenticationPrincipal AuthMember authMember,
//            @RequestBody CommentRequestDto requestDto){
//
//        CommentResponseDto responseDto = commentService.createComment(portfolioId, authMember.getId(), requestDto);
//        return ResponseEntity.ok(ApiResponse.of(responseDto));
//    }
//
//    @Member
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getComments(
//            @PathVariable("portfolioId") Long portfolioId
//    ){
//
//        List<CommentResponseDto> responseDto = commentService.getComments(portfolioId);
//        return ResponseEntity.ok(ApiResponse.of(responseDto));
//    }
//
//
//    @Member
//    @PutMapping("/{commentId}")
//    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
//            @PathVariable("portfolioId") Long portfolioId,
//            @PathVariable("commentId") Long commentId,
//            @AuthenticationPrincipal AuthMember authMember,
//            @RequestBody CommentRequestDto requestDto
//    ){
//
//        CommentResponseDto responseDto = commentService.updateComment(portfolioId, commentId, authMember.getId(), requestDto);
//        return ResponseEntity.ok(ApiResponse.of(responseDto));
//    }
//
//    @Member
//    @DeleteMapping("/{commentId}")
//
//}
