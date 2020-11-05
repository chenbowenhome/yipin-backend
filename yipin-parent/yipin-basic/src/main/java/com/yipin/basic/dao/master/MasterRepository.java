package com.yipin.basic.dao.master;

import com.yipin.basic.entity.master.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MasterRepository extends JpaRepository<Master,Integer> {
    Master findMasterById(Integer id);

    /**获取前十名大师**/
    @Query(value = "SELECT * FROM master LIMIT 10",nativeQuery = true)
    List<Master> getTopTenMaster();
}
