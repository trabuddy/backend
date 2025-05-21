package com.ssafy.trabuddy.domain.sigungu.model.entity;

import com.ssafy.trabuddy.domain.area.model.entity.AreaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "sigungu")
public class SigunguEntity {
    @Id
    private int sigunguCode;
    @ManyToOne
    @JoinColumn(name = "area_code", nullable = false)
    private AreaEntity area;
    private String name;
}
