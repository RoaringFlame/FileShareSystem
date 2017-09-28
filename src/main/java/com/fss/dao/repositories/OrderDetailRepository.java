package com.fss.dao.repositories;

import com.fss.dao.domain.OrderDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends BaseRepository<OrderDetail> {

    /**
     * 查询某物品所有状态
     */
    List<OrderDetail> findByGoodsId(Long GoodsId);

    /**
     * 查询某订单下的所有商品
     */
    List<OrderDetail> findByOrderId(Long orderId);
}
