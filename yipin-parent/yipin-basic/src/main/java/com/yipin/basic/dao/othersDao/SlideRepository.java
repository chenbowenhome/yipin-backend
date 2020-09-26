package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.Slide;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SlideRepository extends JpaRepository<Slide,Integer> {
    Slide findSlideById(Integer id);
    Page<Slide> findAllByOrderByOrderNumAsc(Pageable pageable);
    List<Slide> findSlideByOrderByOrderNumAsc();
    @Query(value = "SELECT * FROM slide ORDER BY order_num ASC LIMIT 3",nativeQuery = true)
    List<Slide> findThreeSlide();
}
