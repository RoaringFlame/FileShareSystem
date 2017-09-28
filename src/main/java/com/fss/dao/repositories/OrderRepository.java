package com.fss.dao.repositories;

import com.fss.dao.enums.OrderStatus;
import com.fss.dao.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends BaseRepository<Order> {
    /**
     *查询某买家所有订单
     */
    Page<Order> findByBuyerIdOrderByCreateTimeDesc(Long buyerId, Pageable pageable);

    /**
     * 查询买家某状态的订单
     * @param buyerId               买家id
     * @param orderStatus           订单状态
     */
    Page<Order> findByBuyerIdAndStateOrderByCreateTimeDesc(Long buyerId, OrderStatus orderStatus, Pageable pageable);
}
