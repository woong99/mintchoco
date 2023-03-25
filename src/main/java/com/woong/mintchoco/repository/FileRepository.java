package com.woong.mintchoco.repository;

import com.woong.mintchoco.domain.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<AttachFile, Long> {
}
