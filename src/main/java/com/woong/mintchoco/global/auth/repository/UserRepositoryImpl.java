package com.woong.mintchoco.global.auth.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woong.mintchoco.global.auth.entity.QUser;
import com.woong.mintchoco.global.auth.model.UserVO;
import com.woong.mintchoco.global.file.entity.QAttachFile;
import java.time.LocalDateTime;
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
                    .set(List.of(qUser.name, qUser.email, qUser.businessNumber, qUser.tel, qUser.updatedAt),
                            List.of(userVO.getName(), userVO.getEmail(), userVO.getBusinessNumber(), userVO.getTel(),
                                    LocalDateTime.now()))
                    .where(qUser.userId.eq(userVO.getUserId()))
                    .execute();
        } else {
            jpaQueryFactory
                    .update(qUser)
                    .set(List.of(qUser.name, qUser.email, qUser.businessNumber, qUser.tel, qUser.profileImage,
                                    qUser.updatedAt),
                            List.of(userVO.getName(), userVO.getEmail(), userVO.getBusinessNumber(), userVO.getTel(),
                                    userVO.getProfileImage(), LocalDateTime.now()))
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
                .set(qUser.updatedAt, LocalDateTime.now())
                .where(qUser.userId.eq(userVO.getUserId()))
                .execute();
        jpaQueryFactory
                .delete(qAttachFile)
                .where(qAttachFile.id.eq(attachFileId))
                .execute();
    }

    /**
     * 사용자의 비밀번호를 변경한다.
     *
     * @param userVO 유저 정보
     */
    @Override
    public void updateUserPwd(UserVO userVO) {
        jpaQueryFactory
                .update(qUser)
                .set(qUser.userPwd, userVO.getNewPwd())
                .set(qUser.updatedAt, LocalDateTime.now())
                .where(qUser.userId.eq(userVO.getUserId()))
                .execute();
    }
}
