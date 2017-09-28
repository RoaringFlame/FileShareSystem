package com.fss.dao.repositories.impl;

import com.eshore.fss.sysmgr.dao.FileVersionDao;
import com.eshore.fss.sysmgr.pojo.FileVersion;
import com.eshore.khala.core.data.jpa.dao.impl.JpaDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class FileVersionDaoImpl extends JpaDaoImpl<FileVersion> implements FileVersionDao{

}
