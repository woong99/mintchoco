package com.woong.mintchoco.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woong.mintchoco.domain.QAttachFile;
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
    QAttachFile qAttachFile = QAttachFile.attachFile;

    /**
     * 사용자의 정보를 업데이트한다. 프로필 사진의 유무에 따라 다른 업데이트 쿼리를 실행한다.
     *
     * @param userVO 유저 정보
     */
    @Override
    public void updateUserInfo(UserVO userVO) {
        if (userVO.getProfileImage() == null) {
            jpaQueryFactory
                    .update(qUser)
                    .set(List.of(qUser.name, qUser.email, qUser.businessNumber, qUser.tel),
                            List.of(userVO.getName(), userVO.getEmail(), userVO.getBusinessNumber(), userVO.getTel()))
                    .where(qUser.userId.eq(userVO.getUserId()))
                    .execute();
        } else {
            jpaQueryFactory
                    .update(qUser)
                    .set(List.of(qUser.name, qUser.email, qUser.businessNumber, qUser.tel, qUser.profileImage),
                            List.of(userVO.getName(), userVO.getEmail(), userVO.getBusinessNumber(), userVO.getTel(),
                                    userVO.getProfileImage()))
                    .where(qUser.userId.eq(userVO.getUserId()))
                    .execute();
        }
    }

    /**
     * 사용자의 프로필 사진을 삭제한다.
     *
     * @param userVO 유저 정보
     */
    @Override
    public void deleteUserProfileImage(UserVO userVO) {
        Long attachFileId = userVO.getProfileImage().getId();
        userVO.setProfileImage(null);
        jpaQueryFactory
                .update(qUser)
                .set(qUser.profileImage, userVO.getProfileImage())
                .where(qUser.userId.eq(userVO.getUserId()))
                .execute();
        jpaQueryFactory
                .delete(qAttachFile)
                .where(qAttachFile.id.eq(attachFileId))
                .execute();
    }
}
