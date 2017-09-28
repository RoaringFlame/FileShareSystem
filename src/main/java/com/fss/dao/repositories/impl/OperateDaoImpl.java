package com.fss.dao.repositories.impl;

import com.eshore.fss.sysmgr.dao.FileReceiveDao;
import com.eshore.fss.sysmgr.dao.FileVersionDao;
import com.eshore.fss.sysmgr.dao.OperateDao;
import com.eshore.fss.sysmgr.pojo.FileReceive;
import com.eshore.fss.sysmgr.pojo.FileVersion;
import com.eshore.fss.sysmgr.pojo.Operate;
import com.eshore.fss.vo.HistorySearchKeys;
import com.eshore.khala.common.model.PageConfig;
import com.eshore.khala.common.utils.type.ObjectUtils;
import com.eshore.khala.core.data.jpa.dao.impl.JpaDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class OperateDaoImpl extends JpaDaoImpl<Operate> implements OperateDao {

    @Autowired
    private FileReceiveDao fileReceiveDao;

    @Autowired
    private FileVersionDao fileVersionDao;

    @Override
    @Transactional
    public List<Map<String, Object>> searchHistoryAndCount(HistorySearchKeys historySearchKeys, PageConfig pageConfig) {
        List<Map<String, Object>> result = this.searchHistory(historySearchKeys, pageConfig);
        List<Object[]> count = this.querySql(pageConfig.getOrderBy(), new Object[]{});
        int allRowCount = ObjectUtils.toInt(count.get(0));
        //暂时将所有查询列数量放入rowCount,后续计算
        pageConfig.setRowCount(allRowCount);
        return result;
    }

    @Override
    @Transactional
    public void downloadFile(FileReceive fileReceive) {
        //写操作表
        Date date = new Date();
        Operate operate = new Operate();
        String operateId = UUID.randomUUID().toString();
        operate.setId(operateId);
        operate.setUserId(fileReceive.getUserId());
        operate.setVersionId(fileReceive.getVersionId());
        operate.setOperateTime(date);
        operate.setOperateFlag(1);
        this.save(operate);

        //写接收表
        if( !fileReceive.getIsReceived() ) { //如果未下载
            fileReceive.setIsReceived(true);
            fileReceive.setDownloadTime(date);
            fileReceiveDao.update(fileReceive);
        }

        //写版本表，下载量+1
        FileVersion fileVersion = fileVersionDao.get(fileReceive.getVersionId());
        fileVersion.setCount(fileVersion.getCount() + 1);
        fileVersionDao.update(fileVersion);
    }


    @Transactional
    private List<Map<String, Object>> searchHistory(HistorySearchKeys historySearchKeys, PageConfig pageConfig) {
        String selectSql = "select v.id as version_id,v.number as version_number,v.count,u.name,u.role,u.department,o.operate_time";

        StringBuilder fromSql;
        fromSql = new StringBuilder(" from t_operate o,t_file_version v,t_file f,t_user u");
        int searchFlag = historySearchKeys.getFlag();
        if (searchFlag == 1) {
            fromSql.append(" where o.operate_flag = 1 and v.id = o.version_id and v.file_id = ").append("'")
                    .append(historySearchKeys.getFileId()).append("'");
        } else if (searchFlag == 2) {
            fromSql.append(" where o.operate_flag = 2 and v.id = o.version_id and v.file_id = ")
                    .append("'").append(historySearchKeys.getFileId()).append("'");
        } else if (searchFlag == 3) {
            fromSql.append(" where o.version_id =").append("'").append(historySearchKeys.getVersionId()).append("'")
                    .append(" and o.operate_flag = 1 and v.id = o.version_id");
        }
        fromSql.append(" and f.id =v.file_id and u.id = o.user_id");

        pageConfig.setOrderBy("select count(*)" + fromSql.toString());

        StringBuilder orderSql;
        orderSql = new StringBuilder();
        orderSql.append(" order by o.operate_time desc");
        int pageNum = pageConfig.getPageNum();
        int pageSize = pageConfig.getPageSize();
        orderSql.append(" limit ").append((pageNum-1)*pageSize).append(",").append(pageSize);
        return this.getSql(selectSql + fromSql.toString() + orderSql.toString());
    }
}
