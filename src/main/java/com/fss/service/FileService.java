package com.fss.service;

import com.fss.controller.vo.FileUploadInit;
import com.fss.controller.vo.FileUploadParam;
import com.fss.controller.vo.JsonResultVO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface FileService {
    FileUploadInit getCatalogAndUserSelector(String userId);

    @Transactional(propagation= Propagation.REQUIRED, rollbackFor = Exception.class)
    JsonResultVO upload(String author,MultipartFile file,
            FileUploadParam fileUploadParam, HttpServletRequest request);
}
