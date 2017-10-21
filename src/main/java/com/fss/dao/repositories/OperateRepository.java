package com.fss.dao.repositories;

import com.fss.dao.domain.Operate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public interface OperateRepository extends BaseRepository<Operate>{

    /**
     * 根据断言查询文件历史记录
     */
    Page<Operate> findByOperateFlagAndFileVersionFileIdOrderByCreateTimeDesc
            (Integer operateFlag,String FileVersionFileId,Pageable pageRequest);


    Page<Operate> findByOperateFlagAndFileVersionIdOrderByCreateTimeDesc
            (Integer operateFlag,String fileVersionId,Pageable pageRequest);
}
