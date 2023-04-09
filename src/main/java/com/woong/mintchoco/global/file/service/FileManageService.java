package com.woong.mintchoco.global.file.service;

import com.woong.mintchoco.global.file.entity.AttachFile;
import com.woong.mintchoco.global.file.repository.FileRepository;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileManageService {

    private final FileRepository fileRepository;
    @Value("${file.dir}")
    private String fileDir;
    @Value("${file.image.ext}")
    private String allowedExt;
    @Value("${file.image.max-size}")
    private long maxSize;

    public AttachFile saveFile(MultipartFile files) throws IOException {
        if (files.isEmpty()) {
            return null;
        }

        File folder = new File(fileDir);

        // 원래 파일 이름 추출
        String originName = files.getOriginalFilename();
        if (originName == null || originName.equals("")) {
            return null;
        }

        // 파일 이름으로 쓸 uuid 생성
        String uuid = UUID.randomUUID().toString();

        // 확장자 추출(ex : .png)
        String extension = originName.substring(originName.lastIndexOf("."));
        if (!allowedExt.contains(extension)) {
            return null;
        }

        // uuid와 확장자 결합
        String savedName = uuid + extension;

        // 파일을 불러올 때 사용할 파일 경로
        String savedPath = fileDir + savedName;

        // 파일 엔티티 생성
        AttachFile file = AttachFile.builder()
                .originName(originName)
                .savedName(savedName)
                .savedPath("/upload/" + savedName)
                .build();

        if (!folder.exists()) {
            try {
                folder.mkdir();
                log.info("폴더가 생성되었습니다.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 실제로 로컬에 uuid를 파일명으로 저장
        files.transferTo(new File(savedPath));

        // 데이터베이스에 파일 정보 저장
        return fileRepository.save(file);
    }
}
