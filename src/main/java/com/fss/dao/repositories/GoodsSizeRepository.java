package com.fss.dao.repositories;

import com.fss.dao.domain.GoodsSize;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 尺码表
 */
@Repository
public interface GoodsSizeRepository extends BaseRepository<GoodsSize> {
    /**
     * 根据商品类型查找商品尺寸
     * @param goodsTypeId               商品类型ID
     * @return                          商品尺寸list
     */
    List<GoodsSize> findByGoodsTypeId(Long goodsTypeId);
}
