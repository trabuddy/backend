package com.ssafy.trabuddy.domain.member.model.entity;

import com.ssafy.trabuddy.domain.member.model.enums.MemberRole;
import com.ssafy.trabuddy.domain.member.model.enums.MemberSex;
import com.ssafy.trabuddy.domain.member.model.enums.MemberSocialType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    private long socialId;
    @Enumerated(EnumType.STRING)
    private MemberSocialType socialType;

    private String socialToken;
    private String mbti;
    private int age;
    @Enumerated(EnumType.STRING)
    private MemberSex sex;

    @CreatedDate
    private LocalDateTime joinedAt;

    private LocalDateTime unregisteredAt;

    @Builder
    public MemberEntity(long socialId, MemberSocialType socialType) {
        this.socialId = socialId;
        this.socialType = socialType;
    }
}
