package com.ssafy.trabuddy.domain.point.repository.query;

import com.ssafy.trabuddy.domain.attraction.model.dto.AttractionSearchRequest;
import com.ssafy.trabuddy.domain.attraction.model.entity.AttractionEntity;
import com.ssafy.trabuddy.domain.member.model.entity.MemberEntity;
import com.ssafy.trabuddy.domain.point.model.entity.PointEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointRepositoryCustom {
    public PointEntity findMemberById(long id);
}
