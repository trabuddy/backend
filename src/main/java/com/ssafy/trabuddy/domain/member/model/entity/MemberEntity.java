package com.ssafy.trabuddy.domain.member.model.entity;

import com.ssafy.trabuddy.domain.member.model.enums.MemberRole;
import com.ssafy.trabuddy.domain.member.model.enums.MemberSex;
import com.ssafy.trabuddy.domain.member.model.enums.MemberSocialType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
@Getter
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    private long socialId;
    @Enumerated(EnumType.STRING)
    private MemberSocialType socialType;

    private String socialToken;
    @Setter
    private String mbti;
    @Setter
    private int age;
    private String nickname;
    @Enumerated(EnumType.STRING)
    @Setter
    private MemberSex sex;

    @CreatedDate
    private LocalDateTime joinedAt;

    private LocalDateTime unregisteredAt;

    @Builder
    public MemberEntity(long socialId, MemberSocialType socialType, String nickname) {
        this.socialId = socialId;
        this.socialType = socialType;
        this.nickname = nickname;
    }
}
