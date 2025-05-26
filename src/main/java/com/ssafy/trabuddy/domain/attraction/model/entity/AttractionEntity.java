package com.ssafy.trabuddy.domain.attraction.model.entity;

import com.ssafy.trabuddy.domain.area.model.entity.AreaEntity;
import com.ssafy.trabuddy.domain.sigungu.model.entity.SigunguEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attraction")
public class AttractionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long attractionId;

    private String contentId;
    private String contentTypeId;
    private String title;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private String telephone;
    private String address1;
    private String address2;
    private String zipCode;
    private String category1;
    private String category2;
    private String category3;
    private double latitude;
    private double longitude;
    private byte mapLevel;
    private String firstImageUrl;
    private String firstImageThumbnailUrl;
    private String copyrightDivisionCode;
    private String booktourInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sigungu_code")
    private SigunguEntity sigungu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_code")
    private AreaEntity area;


}
