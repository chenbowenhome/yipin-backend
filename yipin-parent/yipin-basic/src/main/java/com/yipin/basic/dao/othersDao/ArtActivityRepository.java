package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.ArtActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtActivityRepository extends JpaRepository<ArtActivity,Integer> {
    ArtActivity findArtActivityById(Integer id);
    Page<ArtActivity> findArtActivityByOrderByCreateTimeDesc(Pageable pageable);
}
