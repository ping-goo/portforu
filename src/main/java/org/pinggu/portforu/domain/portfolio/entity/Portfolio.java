package org.pinggu.portforu.domain.portfolio.entity;

import jakarta.persistence.*;
import lombok.*;
import org.pinggu.portforu.common.annotation.SoftDelete;
import org.pinggu.portforu.common.domain.BaseEntity;
import org.pinggu.portforu.domain.member.entity.Member;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name ="portfolios")
@SoftDelete(sql = "UPDATE portfolios SET isDeleted = true WHERE id = ?")
public class Portfolio extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id" , nullable = false)
    private Member member;

    @Column(nullable = false,length = 50)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(length = 500)
    private String fileUrl;

    @Column(nullable = false)
    private Integer views;

    @Builder
    public Portfolio(Member member, String title, String description, String fileUrl, Integer views) {
        this.member = member;
        this.title = title;
        this.description = description;
        this.fileUrl = fileUrl;
        this.views = views;
    }

    public void incrementViews() {
        if (this.views == null) {
            this.views = 1;
        } else {
            this.views++;
        }
    }

    public void update(String newTitle, String newDescription, String newFileUrl) {
        if (newTitle != null && !newTitle.isBlank()) {
            this.title = newTitle;
        }
        if (newDescription != null && !newDescription.isBlank()) {
            this.description = newDescription;
        }
        if (newFileUrl != null && !newFileUrl.isBlank()) {
            this.fileUrl = newFileUrl;
        }
    }

}
