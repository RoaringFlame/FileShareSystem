package com.fss.service.impl;

import com.fss.controller.vo.FileInfoVO;
import com.fss.controller.vo.FileSearchKeys;
import com.fss.controller.vo.PageVO;
import com.fss.dao.domain.File;
import com.fss.dao.domain.FileReceive;
import com.fss.dao.domain.User;
import com.fss.dao.enums.Department;
import com.fss.dao.repositories.FileReceiveRepository;
import com.fss.service.FileReceiveService;
import com.fss.util.PageConfig;
import com.fss.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileReceiveServiceImpl implements FileReceiveService {

    private static final int USER_ALERT = 1; //查询提示文件
    private static final int USER_RECEIVE = 2; //查询未接收文件
    private static final int USER_RECEIVED = 3;//查询已接收文件

    @Autowired
    private FileReceiveRepository fileReceiveRepository;

    /**
     * 查询当前用户待接收文件
     */
    @Override
    public PageVO<FileInfoVO> getPageFileNeedReceive(FileSearchKeys fileSearchKeys, PageConfig pageConfig) {
        fileSearchKeys.setSearchFlag(USER_RECEIVE);
        return getPageReceive(fileSearchKeys,pageConfig);
    }

    /**
     *查询当前用户已接收文件
     */
    @Override
    public PageVO<FileInfoVO> getPageFileReceived(FileSearchKeys fileSearchKeys, PageConfig pageConfig) {
        fileSearchKeys.setSearchFlag(USER_RECEIVED);
        return getPageReceive(fileSearchKeys,pageConfig);
    }

    private PageVO<FileInfoVO> getPageReceive(FileSearchKeys fileSearchKeys, PageConfig pageConfig){
        Specification<FileReceive> specification = getWhereClause(fileSearchKeys);
        Page<FileReceive> fileReceivePage = fileReceiveRepository.findAll(specification,
                new PageRequest(pageConfig.getPageNum()-1,pageConfig.getPageSize(),
                        new Sort(Sort.Direction.DESC,"createTime")));
        List<FileInfoVO> list = this.loadReceiveInfo(fileReceivePage.getContent());
        return (PageVO<FileInfoVO>) PageUtil.generateBy(fileReceivePage,list);
    }

    private List<FileInfoVO> loadReceiveInfo(List<FileReceive> list) {
        List<FileInfoVO> fileInfoVOList = new ArrayList<>();
        for(FileReceive fileReceive:list){
            FileInfoVO fileInfoVO = new FileInfoVO();
            fileInfoVO.setVersionId(fileReceive.getFileVersion().getId());
            fileInfoVO.setFileId(fileReceive.getFile().getId());
            fileInfoVO.setFileName(fileReceive.getFile().getFileName());
            fileInfoVO.setName(fileReceive.getReceiver().getName());
            fileInfoVO.setCatalog(fileReceive.getFile().getCatalog().getName());
            fileInfoVO.setDepartment(fileReceive.getReceiver().getDepartment().getText());
            fileInfoVO.setVersionNumber(fileReceive.getFileVersion().getNumber());
            fileInfoVO.setAlert(fileReceive.getIsAlert());
            if(!fileReceive.getIsReceived()){
                fileInfoVO.setCreateTime(fileReceive.getCreateTime());
            }else{
                fileInfoVO.setDownloadTime(fileReceive.getDownloadTime());
                fileInfoVO.setCanRevise(fileReceive.getCanRevise());
            }
            fileInfoVOList.add(fileInfoVO);
        }
        return fileInfoVOList;
    }

    private Specification<FileReceive> getWhereClause(final FileSearchKeys fileSearchKeys) {
        return new Specification<FileReceive>() {
            @Override public Predicate toPredicate(Root<FileReceive> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                Join<FileReceive,User> userJoin = root.join(root.getModel().getSingularAttribute("receiver",User.class),JoinType.LEFT);
                predicates.add(cb.equal(userJoin.get("id").as(String.class),fileSearchKeys.getUserId()));
                int searchFlag = fileSearchKeys.getSearchFlag();
                if (searchFlag == USER_ALERT) {
                    predicates.add(cb.equal(root.get("isAlert").as(Boolean.class), true));
                    predicates.add(cb.equal(root.get("isReceived").as(Boolean.class), false));
                } else if (searchFlag == USER_RECEIVE) {
                    predicates.add(cb.equal(root.get("isReceived").as(Boolean.class), false));
                } else if (searchFlag == USER_RECEIVED) {
                    predicates.add(cb.equal(root.get("isReceived").as(Boolean.class), true));
                }
                String fileNameKey = fileSearchKeys.getFileNameKey();
                String catalogKey = fileSearchKeys.getCatalogKey();
                String authorNameKey = fileSearchKeys.getNameKey();
                String departmentKey = fileSearchKeys.getDepartmentKey();
                Join<FileReceive, File> fileJoin = root
                        .join(root.getModel().getSingularAttribute("file", File.class), JoinType.LEFT);
                if (!StringUtils.isEmpty(fileNameKey)) {
                    predicates.add(cb.like(fileJoin.get("fileName").as(String.class), "%" + fileNameKey + "%"));
                }
                if (!StringUtils.isEmpty(catalogKey)) {
                    predicates.add(cb.like(fileJoin.get("catalog").get("name").as(String.class), "%" + catalogKey + "%"));
                }
                if (!StringUtils.isEmpty(authorNameKey)) {
                    predicates.add(cb.like(fileJoin.get("author").get("name").as(String.class), "%" + authorNameKey + "%"));
                }
                if (!StringUtils.isEmpty(departmentKey)){
                    int index = Integer.valueOf(departmentKey);
                    Department department = Department.values()[index];
                    predicates.add(cb.equal(fileJoin.get("author").get("department").as(Department.class), department));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                return query.where(predicates.toArray(pre)).getRestriction();
            }
        };
    }

}
