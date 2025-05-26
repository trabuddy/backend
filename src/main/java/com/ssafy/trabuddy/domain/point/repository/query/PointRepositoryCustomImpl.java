package com.ssafy.trabuddy.domain.point.repository.query;

import com.ssafy.trabuddy.domain.plan.model.entity.QPlanEntity;
import com.ssafy.trabuddy.domain.planShare.model.entity.QPlanShareEntity;
import com.ssafy.trabuddy.domain.point.model.entity.PointEntity;
import com.ssafy.trabuddy.domain.point.model.entity.QPointEntity;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class PointRepositoryCustomImpl extends QuerydslRepositorySupport implements PointRepositoryCustom {

    public PointRepositoryCustomImpl() {
        super(PointEntity.class);
    }

    @Override
    public PointEntity findMemberById(long id) {
        QPointEntity point = QPointEntity.pointEntity;
        QPlanShareEntity planShare = QPlanShareEntity.planShareEntity;
        QPlanEntity plan = QPlanEntity.planEntity;

        return from(point)
                .join(point.plan, plan)
                .join(point.plan.participants, planShare)
                .where(point.pointId.eq(id))
                .fetchJoin()
                .fetchOne();
    }
}
