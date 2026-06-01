package com.codeit.mvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            // 이미지
            ".jpg", ".jpeg", ".png", ".gif", ".webp", ".bmp", ".svg",
            // 문서
            ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
            // 텍스트
            ".txt", ".md", ".csv", ".json"
    );

    private final Path uploadPath;

    public FileService(@Value("${blog.file-directory}") String uploadDir) {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(uploadPath);
            log.info("업로드 디렉토리 준비 완료: {}", uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("업로드 디렉토리 생성 실패: "+uploadPath, e);
        }

    }

    public String saveFile(MultipartFile file) {

        // cleanPath - null 체크 및 정규화(상대경로 등을 정리)
        String originFileName= StringUtils.cleanPath(
                file.getOriginalFilename()==null? "unkown":file.getOriginalFilename()
        );
        // file.getOriginalFilename()
        // 바로 저장해도 상관은 없으나 중복의 가능성 있음, 자체적으로 난수 생성해 붙여 넣자
        // 확장자, 파일명 동일한 파일이 들어올 가능성 존재

        // 확장자만 추출하기 (우리가 허용하는 확장자인지를 검사하기 위해)
        int dotIndex = originFileName.lastIndexOf(".");
        // 뒤에서부터 탐색해 "."이 몇 번째에 있는지 찾음
        // 보통 파일 이름 temp.jpg
        String extension=(dotIndex>=0)? originFileName.substring(dotIndex).toLowerCase()
                : "";

        // 허용 확장자 검증
        if (!ALLOWED_EXTENSIONS.contains(extension)){
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다: "+extension); // +) set으로 확장자 명시해줘도 보기 좋음
        }

        // 유효성 검증 로직

        // 랜덤 문자열을 UUID를 이용해 생성 후 확장자를 붙여 저장할 파일명 새롭게 생성
        String savedFileName = UUID.randomUUID().toString().replace("-", "") + extension;

        // 위에 준비한 uploadPath와 새로 생성한 파일명을 이어 붙인 Path 객체를 생성
        Path targetPath = uploadPath.resolve(savedFileName).normalize();
        try {
            file.transferTo(targetPath); // 지정한 경로로 실제 파일을 저장하는 핵심 메서드 (위에 작성한 건 유효성 검증일 뿐)
            log.info("파일 저장 완료: {} (원본: {}, 크기: {} bytes", savedFileName, originFileName, file.getSize());
            return savedFileName; // 파일 저장이 완료되면 새로운 파일명을 리턴 -> DB 저장(현재는 우선 맵에 저장)
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: "+originFileName, e);
        }
    }


}
