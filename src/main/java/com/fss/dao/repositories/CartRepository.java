package com.fss.dao.repositories;

import com.fss.dao.domain.Cart;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends BaseRepository<Cart> {

    /**
     * 用户购物车中商品列表
     * @return                  商品list
     */
    List<Cart> findByUserId(Long userId);

    /**
     * 商品ID查购物车信息
     * @param goodsId           商品ID
     * @param userId            用户ID
     * @return                  购物车
     */
    Cart findByGoodsIdAndUserId(Long goodsId, Long userId);
}
