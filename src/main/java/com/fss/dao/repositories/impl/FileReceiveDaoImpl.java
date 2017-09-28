package com.fss.dao.repositories.impl;

import com.eshore.fss.sysmgr.dao.FileReceiveDao;
import com.eshore.fss.sysmgr.pojo.FileReceive;
import com.eshore.khala.core.data.jpa.dao.impl.JpaDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository public class FileReceiveDaoImpl extends JpaDaoImpl<FileReceive> implements FileReceiveDao {
    @Override public FileReceive getReceiveBy(String userId, String versionId) {
        String hql = "from FileReceive fr where fr.userId = ? and fr.versionId = ?";
        List<String> params = new ArrayList<>();
        params.add(userId);
        params.add(versionId);
        return this.getPojo(hql, params.toArray());
    }

    @Override public List<FileReceive> getReceivesBy(String fileVersionId) {
        String hql = "from FileReceive fr where fr.versionId = ?";
        return this.query(hql, new Object[] { fileVersionId });
    }

    @Override
    public List<FileReceive> getReceivesByFileId(String fileId) {
        String hql = "from FileReceive fr where fr.fileId = ?";
        return this.query(hql, new Object[] { fileId });
    }
}
