package com.fss.service.impl;

import com.eshore.fss.enums.Department;
import com.eshore.fss.enums.UserRole;
import com.eshore.fss.sysmgr.dao.FileVersionDao;
import com.eshore.fss.sysmgr.dao.OperateDao;
import com.eshore.fss.sysmgr.pojo.FileReceive;
import com.eshore.fss.sysmgr.pojo.FileVersion;
import com.eshore.fss.sysmgr.pojo.Operate;
import com.eshore.fss.sysmgr.service.IOperateService;
import com.eshore.fss.vo.FileHistoryVo;
import com.eshore.fss.vo.HistorySearchKeys;
import com.eshore.fss.vo.PageVo;
import com.eshore.khala.common.model.PageConfig;
import com.eshore.khala.common.utils.type.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OperateServiceImpl implements IOperateService {


    @Autowired
    private OperateDao operateDao;

    @Autowired
    private FileVersionDao fileVersionDao;

    //// TODO: 2017/9/3 判断用户是否有读取历史数据权限（查接收表以及版本表关联文件）
    @Override
    public PageVo<FileHistoryVo> getPageFileDownloadHistory(
            String userId, HistorySearchKeys historySearchKeys, PageConfig pageConfig) {
        return loadHistoryInfo(historySearchKeys,pageConfig);
    }

    @Override
    public PageVo<FileHistoryVo> getPageFileReviseHistory(
            String userId, HistorySearchKeys historySearchKeys, PageConfig pageConfig) {
        return loadHistoryInfo(historySearchKeys,pageConfig);
    }

    @Override
    public PageVo<FileHistoryVo> getPageVersionDownloadHistory(
            String userId, HistorySearchKeys historySearchKeys, PageConfig pageConfig) {
        return loadHistoryInfo(historySearchKeys,pageConfig);
    }

    @Override
    @CacheEvict(value = "userAlert", key = "#fileReceive.getUserId()")
    public void downloadFile(FileReceive fileReceive) {
        operateDao.downloadFile(fileReceive);
    }

    @Override
    @Transactional
    public void selfDownloadFile(String userId, FileVersion fileVersion) {
        //写操作表
        Date date = new Date();
        Operate operate = new Operate();
        String operateId = UUID.randomUUID().toString();
        operate.setId(operateId);
        operate.setUserId(userId);
        operate.setVersionId(fileVersion.getId());
        operate.setOperateTime(date);
        operate.setOperateFlag(1);
        operateDao.save(operate);

        //写版本表
        fileVersion.setCount(fileVersion.getCount() + 1);
        fileVersionDao.update(fileVersion);
    }

    private PageVo<FileHistoryVo> loadHistoryInfo(
            HistorySearchKeys historySearchKeys, PageConfig pageConfig) {
        List<Map<String, Object>> histList = operateDao.searchHistoryAndCount(historySearchKeys, pageConfig);
        int flag = historySearchKeys.getFlag();
        List<FileHistoryVo> historyVoList = new ArrayList<>();
        for (Map<String, Object> info : histList) {
            FileHistoryVo fileHistoryVo = new FileHistoryVo();
            fileHistoryVo.setVersionId(ObjectUtils.toString(info.get("version_id")));
            fileHistoryVo.setVersionNumber(ObjectUtils.toDouble(info.get("version_number"), 1.0));
            fileHistoryVo.setName(ObjectUtils.toString(info.get("name")));
            int roleId  = ObjectUtils.toInt((info.get("role")));
            fileHistoryVo.setRole(UserRole.values()[roleId].getText());
            int departmentId = ObjectUtils.toInt(info.get("department"), 1);
            fileHistoryVo.setDepartment(Department.values()[departmentId].getText());
            fileHistoryVo.setCount(ObjectUtils.toInt(info.get("count")));
            fileHistoryVo.setOperateTime(ObjectUtils.toDate(info.get("operate_time")));
            historyVoList.add(fileHistoryVo);
        }
        return new PageVo<>(historyVoList, pageConfig);
    }
}
