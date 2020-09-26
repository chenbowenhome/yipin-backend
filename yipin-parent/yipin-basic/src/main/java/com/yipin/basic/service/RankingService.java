package com.yipin.basic.service;

import VO.Result;
import com.yipin.basic.VO.RankingUserVO;
import com.yipin.basic.VO.RankingVO;
import com.yipin.basic.entity.ranking.RankingPeriod;
import com.yipin.basic.entity.ranking.RankingUser;

import java.util.List;

public interface RankingService {
    /**获取排名前20名用户**/
    Result<RankingVO> findTheRanking(Integer period);
    /**获取所有期数**/
    Result<List<RankingPeriod>> findAllPeriod();
    /**获取目标期数用户前后五名**/
    Result<List<RankingUser>> findUserRanking(Integer userId, Integer period);
}
