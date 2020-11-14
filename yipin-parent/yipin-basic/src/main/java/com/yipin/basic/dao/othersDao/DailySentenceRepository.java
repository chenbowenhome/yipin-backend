package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.DailySentence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DailySentenceRepository extends JpaRepository<DailySentence,Integer> {
    List<DailySentence> findDailySentenceByNowStatus(Integer nowStatus);
    DailySentence findDailySentenceById(Integer id);
    Page<DailySentence> findAll(Pageable pageable);
    @Query(value = "SELECT * FROM daily_sentence ORDER BY  RAND() LIMIT 20;",nativeQuery = true)
    List<DailySentence> findTop20DailySentence();
}
