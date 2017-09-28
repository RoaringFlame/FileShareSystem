package com.fss.dao.repositories;

import com.fss.dao.domain.Area;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends BaseRepository<Area>  {
    /**
     * 获取省
     */
    //@Cacheable("MaBaoCache")
    List<Area> findByLevelType(Integer levelType);

    /**
     * 查某级别下级所有
     * @param i                             级别
     * @param parentsId                    父级ID
     * @return
     */
    //@Cacheable("MaBaoCache")
    List<Area> findByLevelTypeAndParentsAreaId(int i, Long parentsId);
}
