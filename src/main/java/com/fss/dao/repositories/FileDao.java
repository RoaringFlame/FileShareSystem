package com.fss.dao.repositories;

import com.eshore.fss.sysmgr.pojo.File;
import com.eshore.fss.vo.FileSearchKeys;
import com.eshore.fss.vo.FileUploadParam;
import com.eshore.khala.common.model.PageConfig;
import com.eshore.khala.core.data.api.dao.IBaseDao;

import java.util.List;
import java.util.Map;

public interface FileDao extends IBaseDao<File> {

    Integer getAlertCount(String userId);

    boolean uploadFile(Map<String, String> fileName, FileUploadParam fileUploadParam);

    List<Map<String, Object>> searchReceive(FileSearchKeys fileSearchKeys, PageConfig pageConfig);

    List<Map<String,Object>> searchUploaded(FileSearchKeys fileSearchKeys, PageConfig pageConfig);

    List<Map<String, Object>> searchFilesAndCount(FileSearchKeys fileSearchKeys, PageConfig pageConfig);

    Integer getNeedReceiveCount(String userId);

    List<File> getFileListBy(String catalogId);
}
