package com.woong.mintchoco.global.auth.repository;

import com.woong.mintchoco.global.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    boolean existsByUserId(String userId);

    User findByUserId(String userId);
}
