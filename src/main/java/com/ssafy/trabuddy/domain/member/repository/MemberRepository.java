package com.ssafy.trabuddy.domain.member.repository;

import com.ssafy.trabuddy.domain.member.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findBySocialIdAndUnregisteredAtIsNull(long socialId);
}
