package com.fss.service;

import com.fss.controller.vo.FileUploadInit;
import com.fss.controller.vo.FileUploadParam;
import com.fss.controller.vo.JsonResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface FileService {
    FileUploadInit getCatalogAndUserSelector(String userId);

    JsonResultVO upload(String author,MultipartFile file,
            FileUploadParam fileUploadParam, HttpServletRequest request);

    void uploadFile(Map<String, String> fileName, FileUploadParam fileUploadParam);
}
