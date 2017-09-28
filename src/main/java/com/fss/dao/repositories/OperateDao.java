package com.fss.dao.repositories;

import com.eshore.fss.sysmgr.pojo.FileReceive;
import com.eshore.fss.sysmgr.pojo.Operate;
import com.eshore.fss.vo.HistorySearchKeys;
import com.eshore.khala.common.model.PageConfig;
import com.eshore.khala.core.data.api.dao.IBaseDao;

import java.util.List;
import java.util.Map;

public interface OperateDao extends IBaseDao<Operate> {

    List<Map<String, Object>> searchHistoryAndCount(HistorySearchKeys historySearchKeys, PageConfig pageConfig);

    void downloadFile(FileReceive fileReceive);
}
