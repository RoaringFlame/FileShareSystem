package com.fss.service.impl;

import com.fss.controller.vo.FileHistoryVO;
import com.fss.controller.vo.HistorySearchKeys;
import com.fss.controller.vo.PageVO;
import com.fss.dao.domain.Operate;
import com.fss.dao.repositories.OperateRepository;
import com.fss.service.OperateService;
import com.fss.util.PageConfig;
import com.fss.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OperateServiceImpl implements OperateService {

    private final int FILE_DOWNLOAD_HIST = 1; //文件下载历史
    private final int FILE_REVISE_HIST = 2; //文件修改历史
    private final int VERSION_FILE_DOWNLOAD_HIST = 3; //版本文件下载历史

    @Autowired
    private OperateRepository operateRepository;

    @Override
    public PageVO<FileHistoryVO> getPageFileDownloadHistory(String userId,
            HistorySearchKeys historySearchKeys, PageConfig pageConfig) {
        return loadHistory(historySearchKeys,pageConfig);
    }

    @Override
    public PageVO<FileHistoryVO> getPageFileReviseHistory(String userId,
            HistorySearchKeys historySearchKeys, PageConfig pageConfig) {
        return loadHistory(historySearchKeys,pageConfig);
    }

    @Override
    public PageVO<FileHistoryVO> getPageVersionDownloadHistory(String userId,
            HistorySearchKeys historySearchKeys, PageConfig pageConfig) {
        return loadHistory(historySearchKeys,pageConfig);
    }

    private PageVO<FileHistoryVO> loadHistory(HistorySearchKeys historySearchKeys, PageConfig pageConfig) {
        PageRequest pageRequest = new PageRequest(pageConfig.getPageNum()-1,pageConfig.getPageSize());
        int searchFlag = historySearchKeys.getFlag();
        Page<Operate> operatePage = null;
        if(searchFlag == FILE_DOWNLOAD_HIST){
            operatePage = operateRepository.findByOperateFlagAndFileVersionFileIdOrderByCreateTimeDesc(
                    1,historySearchKeys.getFileId(),pageRequest);
        }else if(searchFlag == FILE_REVISE_HIST){
            operatePage = operateRepository.findByOperateFlagAndFileVersionFileIdOrderByCreateTimeDesc(
                    2,historySearchKeys.getFileId(),pageRequest);
        }else if(searchFlag == VERSION_FILE_DOWNLOAD_HIST){
            operatePage = operateRepository.findByOperateFlagAndFileVersionIdOrderByCreateTimeDesc(
                    1,historySearchKeys.getVersionId(),pageRequest);
        }
        assert operatePage != null;
        List<FileHistoryVO> list = loadHistoryInfo(operatePage.getContent());
        return (PageVO<FileHistoryVO>) PageUtil.generateBy(operatePage,list);
    }

    private List<FileHistoryVO> loadHistoryInfo(List<Operate> list) {
        List<FileHistoryVO> historyVOList = new ArrayList<>();
        for(Operate operate:list){
            FileHistoryVO fileHistoryVO = new FileHistoryVO();
            fileHistoryVO.setVersionId(operate.getFileVersion().getId());
            fileHistoryVO.setVersionNumber(operate.getFileVersion().getNumber());
            fileHistoryVO.setName(operate.getOperator().getName());
            fileHistoryVO.setRole(operate.getOperator().getRole().getText());
            fileHistoryVO.setDepartment(operate.getOperator().getDepartment().getText());
            fileHistoryVO.setCount(operate.getFileVersion().getCount());
            fileHistoryVO.setOperateTime(operate.getOperateTime());
            historyVOList.add(fileHistoryVO);
        }
        return historyVOList;
    }
}
