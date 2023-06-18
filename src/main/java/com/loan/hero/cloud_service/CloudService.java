package com.loan.hero.cloud_service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudService {
    String uploadFile(MultipartFile file);
}
