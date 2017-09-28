package com.fss.service;

import com.fss.controller.vo.*;
import com.fss.util.PageVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IFileService {

    FileUploadInit getCatalogAndUserSelector(String userId);

    JsonResultVo upload(String author, MultipartFile file, FileUploadParam fileUploadParam, HttpServletRequest request);

    UserAlertVo getAlertById(String userId);

    List<Map<String, Object>> getAlertBy(String userId);

    HomeFileReceiveVo getHomeFileNeedReceive(String userId);

    List<FileInfoVo> getHomeFileReceived(String userId);

    List<FileInfoVo> getHomeFileUploaded(String userId);

    PageVo<FileInfoVo> getPageFileNeedReceive(FileSearchKeys fileSearchKeys, PageConfig pageConfig);

    PageVo<FileInfoVo> getPageFileReceived(FileSearchKeys fileSearchKeys, PageConfig pageConfig);

    PageVo<FileInfoVo> getPageUploaded(FileSearchKeys fileSearchKeys, PageConfig pageConfig);

    JsonResultVo download(String userId, String versionId, HttpServletRequest request, HttpServletResponse response);

    JsonResultVo reviseFile(String userId, MultipartFile file, String fileVersionId, boolean canCover,
            HttpServletRequest request);

    FileUploadInit getReviseSelector(String userId, String fileId);

    JsonResultVo deleteFile(String fileId, String userId);

    JsonResultVo reviseRole(String userId, String fileId, Set<String> canLoadIds, Set<String> canReviseIds);
}
