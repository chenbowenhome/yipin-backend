package com.yipin.basic.dao.ranking;

import com.yipin.basic.entity.ranking.RankingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RankingUserRepository extends JpaRepository<RankingUser,Integer> {
    @Query(value = "SELECT * FROM ranking_user WHERE period=? ORDER BY ranking ASC LIMIT 20",nativeQuery = true)
    List<RankingUser> findTheRanking(Integer period);
    @Query(value = "SELECT * FROM ranking_user WHERE period=?1 ORDER BY ranking ASC LIMIT ?2,?3",nativeQuery = true)
    List<RankingUser> findUserRanking(Integer period,Integer num,Integer right);
    RankingUser findRankingUserByUserIdAndPeriod(Integer userId,Integer period);
}
