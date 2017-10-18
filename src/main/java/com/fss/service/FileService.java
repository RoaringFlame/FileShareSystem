package com.fss.service;

import com.fss.controller.vo.*;
import com.fss.util.PageConfig;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FileService {

    FileUploadInit getCatalogAndUserSelector(String userId);

    @Transactional(propagation= Propagation.REQUIRED, rollbackFor = Exception.class)
    JsonResultVO upload(String author,MultipartFile file,
            FileUploadParam fileUploadParam, HttpServletRequest request);

    PageVO<FileInfoVO> getPageUploaded(FileSearchKeys fileSearchKeys, PageConfig pageConfig);

    @Transactional(propagation= Propagation.REQUIRED, rollbackFor = Exception.class)
    JsonResultVO download(String userId, String versionId, HttpServletRequest request, HttpServletResponse response);
}
