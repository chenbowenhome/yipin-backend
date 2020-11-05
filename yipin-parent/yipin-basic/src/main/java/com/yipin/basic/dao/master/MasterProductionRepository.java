package com.yipin.basic.dao.master;

import com.yipin.basic.entity.master.MasterProduction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterProductionRepository extends JpaRepository<MasterProduction,Integer> {
    MasterProduction findMasterProductionById(Integer id);
    Page<MasterProduction> findAllByMasterId(Integer masterId, Pageable pageable);
}
