package org.pinggu.portforu.domain.portfolio.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pinggu.portforu.common.dto.ApiResponse;
import org.pinggu.portforu.common.exception.CustomException;
import org.pinggu.portforu.domain.portfolio.service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/portfolios/file")
@RequiredArgsConstructor
public class PortfolioUploadController {
    private final S3Service s3Service;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> uploadPortfolioFile(
            @RequestParam("portfolioFile") MultipartFile portfolioFile
    ) {
        try {
            String uploadedUrl = s3Service.uploadPortfolioFile(portfolioFile);
            return ResponseEntity.ok(ApiResponse.of(uploadedUrl));
        } catch (IOException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "파일 업로드 실패");
        }
    }
}
