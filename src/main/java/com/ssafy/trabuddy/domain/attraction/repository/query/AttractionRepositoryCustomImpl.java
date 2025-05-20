package com.ssafy.trabuddy.domain.attraction.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.ssafy.trabuddy.domain.area.model.entity.QAreaEntity;
import com.ssafy.trabuddy.domain.attraction.model.dto.AttractionSearchRequest;
import com.ssafy.trabuddy.domain.attraction.model.entity.AttractionEntity;
import com.ssafy.trabuddy.domain.attraction.model.entity.QAttractionEntity;
import com.ssafy.trabuddy.domain.sigungu.model.entity.QSigunguEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class AttractionRepositoryCustomImpl extends QuerydslRepositorySupport implements AttractionRepositoryCustom {

    public AttractionRepositoryCustomImpl() {
        super(AttractionEntity.class);
    }

    @Override
    public Page<AttractionEntity> searchAttractions(AttractionSearchRequest request, Pageable pageable) {
        QAttractionEntity a      = QAttractionEntity.attractionEntity;
        QAreaEntity     area     = QAreaEntity.areaEntity;
        QSigunguEntity  sigungu  = QSigunguEntity.sigunguEntity;

        BooleanBuilder builder = new BooleanBuilder();
        StringExpression trimTitle = Expressions.stringTemplate("replace({0}, ' ', '')", a.title);

        // areaCode가 null이 아니고 빈 문자열이 아닐 때만 조건 추가
        if (StringUtils.hasText(request.getAreaCode())) {
            try {
                int areaCode = Integer.parseInt(request.getAreaCode());
                builder.and(a.area.areaCode.eq(areaCode));
            } catch (NumberFormatException e) {
                // 잘못된 형식의 areaCode는 무시
            }
        }

        // sigunguCode가 null이 아니고 빈 문자열이 아닐 때만 조건 추가
        if (StringUtils.hasText(request.getSigunguCode())) {
            try {
                int sigunguCode = Integer.parseInt(request.getSigunguCode());
                builder.and(a.sigungu.sigunguCode.eq(sigunguCode));
            } catch (NumberFormatException e) {
                // 잘못된 형식의 sigunguCode는 무시
            }
        }

        // keyword가 null이 아니고 빈 문자열이 아닐 때만 조건 추가
        if (StringUtils.hasText(request.getKeyword())) {
            builder.and(trimTitle.like("%" + request.getKeyword().replace(" ", "") + "%"));
        }

        // ← selectDistinct(a) 로 시작
        JPQLQuery<Long> idQuery = from(a)
                .select(a.attractionId).distinct()            // ← attractionId 에만 DISTINCT
                .join(a.sigungu, sigungu)
                .join(a.sigungu.area,    area)
                .where(builder)
                .orderBy(sigungu.sigunguCode.asc());

        long total = idQuery.fetchCount();
        List<Long> ids = idQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (ids.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, total);
        }

        // 2) 실제 엔티티 + fetchJoin
        List<AttractionEntity> results = from(a)
                .select(a).distinct()
                .join(a.sigungu, sigungu).fetchJoin()
                .join(sigungu.area,    area).fetchJoin()
                .where(a.attractionId.in(ids))
                .fetch();

        // 3) ID 순으로 정렬
        Map<Long, AttractionEntity> map = results.stream()
                .collect(Collectors.toMap(
                        AttractionEntity::getAttractionId,
                        Function.identity(),
                        (existing, replacement) -> existing,     // duplicate key 시 기존 값 유지
                        LinkedHashMap::new                      // 순서를 보장하려면 LinkedHashMap 사용
                ));

        List<AttractionEntity> sorted = ids.stream()
                .map(map::get)
                .filter(Objects::nonNull)                 // 안전하게 null 필터링
                .toList();

        return new PageImpl<>(sorted, pageable, total);
    }
}
