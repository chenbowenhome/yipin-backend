package com.yipin.basic.dao;

import com.yipin.basic.entity.Slide;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SlideRepository extends JpaRepository<Slide,Integer> {
    Slide findSlideById(Integer id);
    Page<Slide> findAllByOrderByOrderNumDesc(Pageable pageable);
}
