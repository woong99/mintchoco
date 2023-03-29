package com.woong.mintchoco.repository.store;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.woong.mintchoco.domain.QStore;
import com.woong.mintchoco.vo.StoreVO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    QStore qStore = QStore.store;

    /**
     * store 를 업데이트한다.
     *
     * @param id      store id
     * @param storeVO 가게 정보
     */
    @Override
    public void updateStore(Long id, StoreVO storeVO) {
        JPAUpdateClause updateClause =
                jpaQueryFactory
                        .update(qStore)
                        .set(
                                List.of(qStore.name, qStore.intro, qStore.zipCode, qStore.address, qStore.latitude,
                                        qStore.longitude, qStore.tel,
                                        qStore.closedDays, qStore.updatedAt),
                                List.of(storeVO.getName(), storeVO.getIntro(), storeVO.getZipCode(),
                                        storeVO.getAddress(), storeVO.getLatitude(), storeVO.getLongitude(),
                                        storeVO.getTel(), storeVO.getClosedDays(),
                                        LocalDateTime.now())
                        )
                        .where(qStore.id.eq(id));

        if (storeVO.getDetailAddress() != null) {
            updateClause.set(qStore.detailAddress, storeVO.getDetailAddress());
        }
        if (storeVO.getWeekdayStart() != null) {
            updateClause.set(qStore.weekdayStart, storeVO.getWeekdayStart());
        } else {
            updateClause.setNull(qStore.weekdayStart);
        }
        if (storeVO.getWeekdayEnd() != null) {
            updateClause.set(qStore.weekdayEnd, storeVO.getWeekdayEnd());
        } else {
            updateClause.setNull(qStore.weekdayEnd);
        }
        if (storeVO.getWeekdayIsOpen() != null) {
            updateClause.set(qStore.weekdayIsOpen, storeVO.getWeekdayIsOpen());
        }
        if (storeVO.getSaturdayStart() != null) {
            updateClause.set(qStore.saturdayStart, storeVO.getSaturdayStart());
        } else {
            updateClause.setNull(qStore.saturdayStart);
        }
        if (storeVO.getSaturdayEnd() != null) {
            updateClause.set(qStore.saturdayEnd, storeVO.getSaturdayEnd());
        } else {
            updateClause.setNull(qStore.saturdayEnd);
        }
        if (storeVO.getSaturdayIsOpen() != null) {
            updateClause.set(qStore.saturdayIsOpen, storeVO.getSaturdayIsOpen());
        }
        if (storeVO.getSundayStart() != null) {
            updateClause.set(qStore.sundayStart, storeVO.getSundayStart());
        } else {
            updateClause.setNull(qStore.sundayStart);
        }
        if (storeVO.getSundayEnd() != null) {
            updateClause.set(qStore.sundayEnd, storeVO.getSundayEnd());
        } else {
            updateClause.setNull(qStore.sundayEnd);
        }
        if (storeVO.getSundayIsOpen() != null) {
            updateClause.set(qStore.sundayIsOpen, storeVO.getSundayIsOpen());
        }
        updateClause.execute();
    }
}
