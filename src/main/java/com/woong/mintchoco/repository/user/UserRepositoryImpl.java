package com.woong.mintchoco.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woong.mintchoco.domain.QUser;
import com.woong.mintchoco.vo.UserVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QUser qUser = QUser.user;

    @Override
    public void updateUserInfo(UserVO userVO) {
        jpaQueryFactory
                .update(qUser)
                .set(List.of(qUser.name, qUser.email, qUser.businessNumber, qUser.tel),
                        List.of(userVO.getName(), userVO.getEmail(), userVO.getBusinessNumber(), userVO.getTel()))
                .where(qUser.userId.eq(userVO.getUserId()))
                .execute();
    }
}
