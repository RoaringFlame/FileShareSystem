package com.fss.service;

import com.fss.controller.vo.*;
import com.fss.util.PageConfig;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

public interface FileService {

    FileUploadInit getCatalogAndUserSelector(String userId);

    @Transactional(propagation= Propagation.REQUIRED, rollbackFor = Exception.class)
    JsonResultVO upload(String author,MultipartFile file,
            FileUploadParam fileUploadParam, HttpServletRequest request);

    PageVO<FileInfoVO> getPageUploaded(FileSearchKeys fileSearchKeys, PageConfig pageConfig);

    List<FileInfoVO> getHomeFileUploaded(String userId);

    @Transactional(propagation= Propagation.REQUIRED, rollbackFor = Exception.class)
    JsonResultVO download(String userId, String versionId, HttpServletRequest request, HttpServletResponse response);

    @Transactional(propagation= Propagation.REQUIRED, rollbackFor = Exception.class)
    JsonResultVO deleteFile(String fileId, String userId);

    FileUploadInit getReviseSelector(String userId, String fileId);

    @Transactional(propagation= Propagation.REQUIRED, rollbackFor = Exception.class)
    JsonResultVO reviseRole(String userId, String fileId, Set<String> canLoadIdSet, Set<String> canReviseSet);

    @Transactional(propagation= Propagation.REQUIRED, rollbackFor = Exception.class)
    JsonResultVO reviseFile(String userId, MultipartFile file, String fileVersionId, boolean canCover,
            HttpServletRequest request);
}
