package org.pinggu.portforu.domain.comment.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.pinggu.portforu.domain.comment.entity.Comment;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private Long memberId;
    private Long portfolioId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static CommentResponseDto from(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .memberId(comment.getMember().getId())
                .portfolioId(comment.getPortfolio().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .deletedAt(comment.getDeletedAt())
                .build();
    }
}
