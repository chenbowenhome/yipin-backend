package com.yipin.basic.dao.ranking;

import com.yipin.basic.entity.ranking.RankingPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RankingPeriodRepository extends JpaRepository<RankingPeriod,Integer> {
    @Query(value = "SELECT * FROM ranking_period ORDER BY period DESC LIMIT 1",nativeQuery = true)
    RankingPeriod findLastRankingPeriod();

    RankingPeriod findRankingPeriodByPeriod(Integer period);

    List<RankingPeriod> findRankingPeriodByOrderByPeriodDesc();
}
