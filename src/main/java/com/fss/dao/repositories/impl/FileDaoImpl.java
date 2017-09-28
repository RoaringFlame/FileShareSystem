package com.fss.dao.repositories.impl;

import com.eshore.fss.sysmgr.dao.FileDao;
import com.eshore.fss.sysmgr.dao.FileReceiveDao;
import com.eshore.fss.sysmgr.dao.FileVersionDao;
import com.eshore.fss.sysmgr.dao.OperateDao;
import com.eshore.fss.sysmgr.pojo.File;
import com.eshore.fss.sysmgr.pojo.FileReceive;
import com.eshore.fss.sysmgr.pojo.FileVersion;
import com.eshore.fss.sysmgr.pojo.Operate;
import com.eshore.fss.vo.FileSearchKeys;
import com.eshore.fss.vo.FileUploadParam;
import com.eshore.khala.common.model.PageConfig;
import com.eshore.khala.common.utils.type.ObjectUtils;
import com.eshore.khala.common.utils.type.StringUtils;
import com.eshore.khala.core.data.jpa.dao.impl.JpaDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class FileDaoImpl extends JpaDaoImpl<File> implements FileDao {

    @Autowired
    private FileVersionDao fileVersionDao;

    @Autowired
    private OperateDao operateDao;

    @Autowired
    private FileReceiveDao fileReceiveDao;

    @Override
    public Integer getAlertCount(String userId) {
        String hql =
                "select count(*) from FileReceive fr where fr.userId = ? " + "and fr.isAlert = 1 and fr.isReceived = 0";
        return this.queryCount(hql, new Object[]{userId});
    }

    @Override
    @Transactional
    public boolean uploadFile(Map<String, String> fileName, FileUploadParam fileUploadParam) {
        Date date = new Date();
        String fileName1 = fileName.get("fileName");
        boolean mailTo = fileUploadParam.isMailTo();
        String userId = fileUploadParam.getUserInfo().getUserId();

        //写入文件表
        File file = new File();
        String fileId = UUID.randomUUID().toString();
        file.setId(fileId);
        file.setUserId(userId);
        file.setCatalogId(fileUploadParam.getCatalogId());
        //此处先生成文件版本编号，直接插入到文件表，省去更新步骤
        String fileVersionId = UUID.randomUUID().toString();
        file.setNewVersionId(fileVersionId);
        file.setFileName(fileName1);
        file.setCreateTime(date);
        file.setUsable(true);
        this.save(file);

        //写入版本表
        FileVersion fileVersion = new FileVersion();
        fileVersion.setId(fileVersionId);
        fileVersion.setUserId(userId);
        fileVersion.setFileId(fileId);
        fileVersion.setNumber(1.0);
        fileVersion.setRealName(fileName.get("realName"));
        fileVersion.setCreateTime(date);
        fileVersion.setCount(0);
        fileVersion.setCanCover(fileUploadParam.isCanCover());
        fileVersionDao.save(fileVersion);

        //写入操作表
        Operate operate = new Operate();
        String operateId = UUID.randomUUID().toString();
        operate.setId(operateId);
        operate.setUserId(userId);
        operate.setVersionId(fileVersionId);
        operate.setOperateTime(date);
        operate.setOperateFlag(0);
        operateDao.save(operate);


        //遍历可下载人员id，新建接收表
        List<String> canLoadIds = fileUploadParam.getCanOnlyLoadUserIds();
        if (canLoadIds != null && canLoadIds.size() > 0) {
            for (String id : canLoadIds) {
                FileReceive fileReceive = new FileReceive();
                String fileReceiveId = UUID.randomUUID().toString();
                fileReceive.setId(fileReceiveId);
                fileReceive.setVersionId(fileVersionId);
                fileReceive.setFileId(fileId);
                fileReceive.setUserId(id);
                fileReceive.setIsAlert(mailTo);
                fileReceive.setIsReceived(false);
                fileReceive.setDownloadTime(date);
                fileReceive.setCanRevise(false);
                fileReceiveDao.save(fileReceive);
            }
        }

        //遍历可修改人员id列表
        List<String> canReviseIds = fileUploadParam.getCanReviseUserIds();
        if (canReviseIds != null && canReviseIds.size() > 0) {
            for (String id : canReviseIds) {
                FileReceive fileReceive = new FileReceive();
                String fileReceiveId = UUID.randomUUID().toString();
                fileReceive.setId(fileReceiveId);
                fileReceive.setVersionId(fileVersionId);
                fileReceive.setFileId(fileId);
                fileReceive.setUserId(id);
                fileReceive.setIsAlert(mailTo);
                fileReceive.setIsReceived(false);
                fileReceive.setDownloadTime(date);
                fileReceive.setCanRevise(true);
                fileReceiveDao.save(fileReceive);
            }
        }
        return true;
    }

    @Override
    @Transactional
    public List<Map<String, Object>> searchReceive(FileSearchKeys fileSearchKeys, PageConfig pageConfig) {
        String selectSql = "select u.name,u.gender,u.role,u.department,v.id as version_id,v.number as version_number," +
                "v.create_time,v.real_name,f.file_name,f.id as file_id,r.is_alert,r.can_revise,r.download_time,c.name as catalog_name";

        StringBuilder fromSql;
        fromSql = new StringBuilder(" from t_file_receive r,t_file_version v,t_file f,t_user u,t_catalog c" +
                " where r.user_id = " +"'"+fileSearchKeys.getUserId()+"'");
        int searchFlag = fileSearchKeys.getSearchFlag();
        if (searchFlag == 1) {
            fromSql.append(" and r.is_alert = 1 and r.is_received = 0");
        } else if (searchFlag == 2) {
            fromSql.append(" and r.is_received = 0");
        } else if (searchFlag == 3) {
            fromSql.append(" and r.is_received = 1");
        }
        fromSql.append(" and v.id = r.version_id and f.id = v.file_id and f.usable = 1");
        if (StringUtils.isNotEmpty(fileSearchKeys.getFileNameKey())) {
            fromSql.append(" and f.file_name like '%").append(fileSearchKeys.getFileNameKey()).append("%'");
        }
        fromSql.append(" and c.id = f.catalog_id");
        if (StringUtils.isNotEmpty(fileSearchKeys.getCatalogKey())) {
            fromSql.append(" and c.name like '%").append(fileSearchKeys.getCatalogKey()).append("%'");
        }
        fromSql.append("  and u.id = f.user_id and u.usable = 1");
        if (StringUtils.isNotEmpty(fileSearchKeys.getDepartmentId())) {
            fromSql.append(" and u.department = ").append(fileSearchKeys.getDepartmentId());
        }
        if (StringUtils.isNotEmpty(fileSearchKeys.getNameKey())) {
            fromSql.append(" and u.name like '%").append(fileSearchKeys.getNameKey()).append("%'");
        }
        pageConfig.setOrderBy("select count(*)" + fromSql.toString());

        StringBuilder orderSql;
        orderSql = new StringBuilder();
        if (searchFlag == 3) {
            orderSql.append(" order by r.download_time desc");
        }else {
            orderSql.append(" order by v.create_time desc");
        }
        int pageNum = pageConfig.getPageNum();
        int pageSize = pageConfig.getPageSize();
        orderSql.append(" limit ").append((pageNum-1)*pageSize).append(",").append(pageSize);
        return this.getSql(selectSql + fromSql.toString() + orderSql.toString());
    }

    @Override
    public List<Map<String, Object>> searchUploaded(FileSearchKeys fileSearchKeys, PageConfig pageConfig) {
        String selectSql = "select f.id as file_id,f.file_name,f.create_time,c.name as catalog_name,v.id as version_id," +
                "v.number as version_number,v.real_name";

        StringBuilder fromSql;
        fromSql = new StringBuilder(" from t_file f,t_file_version v,t_catalog c" +
                " where f.user_id = " +"'"+ fileSearchKeys.getUserId()+"'");
        if (StringUtils.isNotEmpty(fileSearchKeys.getFileNameKey())) {
            fromSql.append(" and f.name like '%").append(fileSearchKeys.getFileNameKey()).append("%'");
        }
        fromSql.append(" and v.id = f.new_version_id and c.id = f.catalog_id and f.usable = 1");
        if (StringUtils.isNotEmpty(fileSearchKeys.getCatalogKey())) {
            fromSql.append(" and c.name like '%").append(fileSearchKeys.getCatalogKey()).append("%'");
        }

        pageConfig.setOrderBy("select count(*)" + fromSql.toString());

        StringBuilder orderSql;
        orderSql = new StringBuilder();
        orderSql.append(" order by f.create_time desc");
        int pageNum = pageConfig.getPageNum();
        int pageSize = pageConfig.getPageSize();
        orderSql.append(" limit ").append((pageNum-1)*pageSize).append(",").append(pageSize);
        return this.getSql(selectSql + fromSql.toString() + orderSql.toString());
    }

    @Override
    @Transactional
    public List<Map<String, Object>> searchFilesAndCount(FileSearchKeys fileSearchKeys, PageConfig pageConfig) {
        List<Map<String, Object>> result;
        if (fileSearchKeys.getSearchFlag() == 4) {
            result = this.searchUploaded(fileSearchKeys, pageConfig);
        } else {
            result = this.searchReceive(fileSearchKeys, pageConfig);
        }
        List<Object[]> count = this.querySql(pageConfig.getOrderBy(), new Object[]{});
        int allRowCount = ObjectUtils.toInt(count.get(0));
        //暂时将所有查询列数量放入rowCount,后续计算
        pageConfig.setRowCount(allRowCount);
        return result;
    }

    @Override
    public Integer getNeedReceiveCount(String userId) {
        String hql = "select count(*) from FileReceive fr,FileVersion fv,File f,User u"
                + " where fr.userId = ? " + " and fr.isReceived = 0 and fv.id = fr.versionId"
                + " and f.id = fv.fileId and f.usable = true and u.id = fv.userId and u.usable=true";
        return this.queryCount(hql, new Object[]{userId});
    }

    @Override public List<File> getFileListBy(String catalogId) {
        String hql = "from File f where f.catalogId = ?";
        return this.query(hql, new Object[]{catalogId});
    }
}
