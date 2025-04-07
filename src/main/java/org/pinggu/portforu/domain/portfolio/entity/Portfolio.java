package org.pinggu.portforu.domain.portfolio.entity;

import jakarta.persistence.*;
import lombok.*;
import org.pinggu.portforu.common.domain.BaseEntity;
import org.pinggu.portforu.domain.member.entity.Member;

@Getter
@Entity
@Table(name ="portfolios")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Portfolio extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id" , nullable = false)
    private Member member;

    @Column(nullable = false,length = 50)
    private String title;

    @Lob
    private String description;

    @Column(length = 500)
    private String fileUrl;

    @Column(nullable = false)
    private Integer views;

    public Portfolio(Member member, String title, String description, String fileUrl, Integer views) {
        this.member = member;
        this.title = title;
        this.description = description;
        this.fileUrl = fileUrl;
        this.views = views;
    }


    public static Portfolio createPortfolio(Member member, String title, String description, String fileUrl) {
        return new Portfolio(member, title, description, fileUrl, 0);
    }

    public void update(String title, String description, String fileUrl) {
        if (title != null) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (fileUrl != null) {
            this.fileUrl = fileUrl;
        }
    }

    public void incrementViews() {
        if (this.views == null) {
            this.views = 1;
        } else {
            this.views++;
        }
    }

}
