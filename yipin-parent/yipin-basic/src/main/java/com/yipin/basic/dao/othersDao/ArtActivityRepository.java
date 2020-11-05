package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.ArtActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtActivityRepository extends JpaRepository<ArtActivity,Integer> {
    ArtActivity findArtActivityById(Integer id);
    Page<ArtActivity> findArtActivityBySlideStatusOrderByCreateTimeDesc(Integer slideStatus,Pageable pageable);
    List<ArtActivity> findArtActivityBySlideStatusOrderByCreateTimeDesc(Integer slideStatus);
}
