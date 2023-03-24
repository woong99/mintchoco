package com.woong.mintchoco.repository.user;

import com.woong.mintchoco.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    boolean existsByUserId(String userId);

    User findByUserId(String userId);
}
