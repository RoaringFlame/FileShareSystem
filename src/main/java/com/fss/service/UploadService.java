package com.fss.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface UploadService {
    public String upload(MultipartFile file, String destDir,
            HttpServletRequest request) throws Exception;
}
