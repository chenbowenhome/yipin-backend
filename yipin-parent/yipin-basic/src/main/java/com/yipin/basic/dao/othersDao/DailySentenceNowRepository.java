package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.DailySentenceNow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DailySentenceNowRepository extends JpaRepository<DailySentenceNow,Integer> {
    Page<DailySentenceNow> findDailySentenceNowByOrderByNowDateDesc(Pageable pageable);
    @Query(value = "SELECT * FROM daily_sentence_now ORDER BY  now_date DESC LIMIT 20;",nativeQuery = true)
    List<DailySentenceNow> findTop20();
    @Query(value = "SELECT * FROM daily_sentence_now ORDER BY  now_date DESC LIMIT 1;",nativeQuery = true)
    DailySentenceNow findTop1();

}
