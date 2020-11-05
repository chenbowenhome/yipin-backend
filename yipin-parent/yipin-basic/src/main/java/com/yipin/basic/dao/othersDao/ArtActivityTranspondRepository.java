package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.ArtActivityTranspond;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtActivityTranspondRepository extends JpaRepository<ArtActivityTranspond,Integer> {
    ArtActivityTranspond findArtActivityTranspondById(Integer id);
}
