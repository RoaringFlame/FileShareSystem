package com.fss.dao.repositories;

import com.fss.dao.domain.Brand;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 品牌
 */
@Repository
public interface BrandRepository extends BaseRepository<Brand> {
    /**
     * 获取启用的品牌
     * @param status            是否启用
     * @return                  品牌list
     */
    List<Brand> findByStatus(Boolean status);
}
