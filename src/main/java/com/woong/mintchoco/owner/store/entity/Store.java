package com.woong.mintchoco.owner.store.entity;

import com.woong.mintchoco.global.common.entity.BaseEntity;
import com.woong.mintchoco.owner.store.model.StoreVO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Store extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 4304254191931937770L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;                // 상호명

    private String intro;               // 소개

    private String zipCode;             // 우편번호

    private String address;             // 주소

    private String detailAddress;       // 상세주소

    private String latitude;            // 위도

    private String longitude;           // 경도

    private String tel;                 // 전화번호

    private String weekdayStart;        // 평일 영업 시작 시간

    private String weekdayEnd;          // 평일 영업 종료 시간

    private String weekdayIsOpen;       // 평일 영업 유무

    private String saturdayStart;       // 토요일 영업 시작 시간

    private String saturdayEnd;         // 토요일 영업 종료 시간

    private String saturdayIsOpen;      // 토요일 영업 유무

    private String sundayStart;         // 일요일 영업 시작 시간

    private String sundayEnd;           // 일요일 영업 종료 시간

    private String sundayIsOpen;        // 일요일 영업 유무

    private String closedDays;          // 휴무일

    public static Store toEntity(StoreVO storeVO) {
        return Store.builder()
                .name(storeVO.getName())
                .intro(storeVO.getIntro())
                .zipCode(storeVO.getZipCode())
                .address(storeVO.getAddress())
                .detailAddress(storeVO.getDetailAddress())
                .latitude(storeVO.getLatitude())
                .longitude(storeVO.getLongitude())
                .tel(storeVO.getTel())
                .weekdayStart(storeVO.getWeekdayStart())
                .weekdayEnd(storeVO.getWeekdayEnd())
                .weekdayIsOpen(storeVO.getWeekdayIsOpen())
                .saturdayStart(storeVO.getSaturdayStart())
                .saturdayEnd(storeVO.getSaturdayEnd())
                .saturdayIsOpen(storeVO.getSaturdayIsOpen())
                .sundayStart(storeVO.getSundayStart())
                .sundayEnd(storeVO.getSundayEnd())
                .sundayIsOpen(storeVO.getSundayIsOpen())
                .closedDays(storeVO.getClosedDays())
                .build();
    }
}
