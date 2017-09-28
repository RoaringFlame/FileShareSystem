package com.fss.dao.repositories;

import com.eshore.fss.sysmgr.pojo.FileReceive;
import com.eshore.khala.core.data.api.dao.IBaseDao;

import java.util.List;

public interface FileReceiveDao extends IBaseDao<FileReceive>{

    FileReceive getReceiveBy(String userId, String versionId);

    List<FileReceive> getReceivesBy(String fileVersionId);

    List<FileReceive> getReceivesByFileId(String fileId);
}
