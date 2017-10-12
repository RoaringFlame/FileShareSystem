package com.fss.dao.repositories;

import com.fss.dao.domain.File;
import com.fss.dao.repositories.custom.FileRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends BaseRepository<File> ,FileRepositoryCustom{

}
