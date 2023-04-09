package com.woong.mintchoco.global.file.repository;

import com.woong.mintchoco.global.file.entity.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<AttachFile, Long> {
}
