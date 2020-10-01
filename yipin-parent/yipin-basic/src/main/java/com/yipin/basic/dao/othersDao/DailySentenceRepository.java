package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.DailySentence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailySentenceRepository extends JpaRepository<DailySentence,Integer> {
    List<DailySentence> findDailySentenceByNowStatus(Integer nowStatus);
    DailySentence findDailySentenceById(Integer id);
}
