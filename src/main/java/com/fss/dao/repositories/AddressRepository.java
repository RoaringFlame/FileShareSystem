package com.fss.dao.repositories;

import com.fss.dao.domain.Address;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends BaseRepository<Address>  {
    /**
     * 查默认收货地址
     */
    Address findByUserIdAndState(Long userId, boolean b);

    /**
     * 显示当前用户的所有收货地址
     * @return                  地址list
     */
    List<Address> findByUserId(Long userId);
}
