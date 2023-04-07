package com.woong.mintchoco.owner.store.model;

import com.woong.mintchoco.owner.store.entity.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class StoreVO {

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

    private String formattedWeekdayOpeningHours;    // 포매팅된 평일 영업 시간

    private String formattedSaturdayOpeningHours;   // 포매팅된 토요일 영업 시간

    private String formattedSundayOpeningHours;     // 포매팅된 일요일 영업 시간

    public static StoreVO toStoreVO(Store store) {
        return StoreVO.builder()
                .name(store.getName())
                .intro(store.getIntro())
                .zipCode(store.getZipCode())
                .address(store.getAddress())
                .detailAddress(store.getDetailAddress())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .tel(store.getTel())
                .weekdayStart(store.getWeekdayStart())
                .weekdayEnd(store.getWeekdayEnd())
                .weekdayIsOpen(store.getWeekdayIsOpen())
                .saturdayStart(store.getSaturdayStart())
                .saturdayEnd(store.getSaturdayEnd())
                .saturdayIsOpen(store.getSaturdayIsOpen())
                .sundayStart(store.getSundayStart())
                .sundayEnd(store.getSundayEnd())
                .sundayIsOpen(store.getSundayIsOpen())
                .closedDays(store.getClosedDays())
                .build();
    }

}
