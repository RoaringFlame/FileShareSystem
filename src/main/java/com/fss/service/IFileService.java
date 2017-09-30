package com.fss.service;

import com.fss.controller.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IFileService {

    FileUploadInit getCatalogAndUserSelector(String userId);

    JsonResultVO upload(String author, MultipartFile file, FileUploadParam fileUploadParam, HttpServletRequest request);

    UserAlertVO getAlertById(String userId);

    List<Map<String, Object>> getAlertBy(String userId);

    HomeFileReceiveVO getHomeFileNeedReceive(String userId);

    List<FileInfoVO> getHomeFileReceived(String userId);

    List<FileInfoVO> getHomeFileUploaded(String userId);

    PageVO<FileInfoVO> getPageFileNeedReceive(FileSearchKeys fileSearchKeys, PageConfig pageConfig);

    PageVO<FileInfoVO> getPageFileReceived(FileSearchKeys fileSearchKeys, PageConfig pageConfig);

    PageVO<FileInfoVO> getPageUploaded(FileSearchKeys fileSearchKeys, PageConfig pageConfig);

    JsonResultVO download(String userId, String versionId, HttpServletRequest request, HttpServletResponse response);

    JsonResultVO reviseFile(String userId, MultipartFile file, String fileVersionId, boolean canCover,
            HttpServletRequest request);

    FileUploadInit getReviseSelector(String userId, String fileId);

    JsonResultVO deleteFile(String fileId, String userId);

    JsonResultVO reviseRole(String userId, String fileId, Set<String> canLoadIds, Set<String> canReviseIds);
}
