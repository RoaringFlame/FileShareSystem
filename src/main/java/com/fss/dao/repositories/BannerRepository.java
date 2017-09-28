package com.fss.dao.repositories;

import com.fss.dao.domain.Banner;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 广告
 * Created by jackie on 2016/07/09.
 */
@Repository
public interface BannerRepository extends BaseRepository<Banner>{
    /**
     * sort倒序查找启用的广告
     * @param status            是否启用
     * @return                  广告对象list
     */
    List<Banner> findByStatusOrderBySortDesc(boolean status);
}
