package com.yipin.basic.service.impl;

import VO.Result;
import com.yipin.basic.VO.RankingUserVO;
import com.yipin.basic.VO.RankingVO;
import com.yipin.basic.dao.ranking.RankingPeriodRepository;
import com.yipin.basic.dao.ranking.RankingUserRepository;
import com.yipin.basic.dao.userDao.UserArtRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.ranking.RankingPeriod;
import com.yipin.basic.entity.ranking.RankingUser;
import com.yipin.basic.entity.user.User;
import com.yipin.basic.entity.user.UserArt;
import com.yipin.basic.service.RankingService;
import enums.ResultEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RankingServiceImpl implements RankingService {

    @Autowired
    private RankingUserRepository rankingUserRepository;
    @Autowired
    private RankingPeriodRepository rankingPeriodRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserArtRepository userArtRepository;

    /**获取排名前20名用户**/
    @Override
    public Result<RankingVO> findTheRanking(Integer period) {
        if (period == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        RankingPeriod rankingPeriod = rankingPeriodRepository.findRankingPeriodByPeriod(period);
        RankingVO rankingVO = new RankingVO();
        BeanUtils.copyProperties(rankingPeriod,rankingVO);
        List<RankingUser> rankingUsers = rankingUserRepository.findTheRanking(period);
        List<RankingUserVO> rankingUserVOList = new ArrayList<>();
        for (RankingUser rankingUser : rankingUsers) {
            User user = userRepository.findUserById(rankingUser.getUserId());
            UserArt userArt = userArtRepository.findLastUserArt(rankingUser.getUserId());
            RankingUserVO rankingUserVO = new RankingUserVO();
            BeanUtils.copyProperties(user,rankingUserVO);
            rankingUserVO.setRanking(rankingUser.getRanking());
            rankingUserVO.setUserArt(userArt);
            rankingUserVOList.add(rankingUserVO);
        }
        rankingVO.setRankingUserVOList(rankingUserVOList);
        return Result.newSuccess(rankingVO);
    }

    /**获取所有期数**/
    @Override
    public Result<List<RankingPeriod>> findAllPeriod(){
        List<RankingPeriod> rankingPeriodList = rankingPeriodRepository.findRankingPeriodByOrderByPeriodDesc();
        return Result.newSuccess(rankingPeriodList);
    }

    /**获取目标期数用户前后五名**/
    @Override
    public Result<List<RankingUserVO>> findUserRanking(Integer userId,Integer period){
        if (userId == null || period == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        RankingUser rankingUser = rankingUserRepository.findRankingUserByUserIdAndPeriod(userId,period);
        if (rankingUser == null){
            return Result.newError("当前用户无排名");
        }
        Integer ranking = rankingUser.getRanking();
        Integer left = 0;
        Integer num = 10;
        if ((ranking - 6) <= 0){
            left = 0;
            num = 10 + (ranking - 6);
        }else{
            left = ranking - 5;
        }
        List<RankingUser> rankingUserList = rankingUserRepository.findUserRanking(period,left,num);
        List<RankingUserVO> rankingUserVOList = new ArrayList<>();
        for (RankingUser user : rankingUserList) {
            RankingPeriod rankingPeriod = rankingPeriodRepository.findRankingPeriodByPeriod(period);
            UserArt userArt = userArtRepository.findLastUserArt(userId);
            User u = userRepository.findUserById(user.getUserId());
            RankingUserVO rankingUserVO = new RankingUserVO();
            BeanUtils.copyProperties(user,rankingUserVO);
            rankingUserVO.setAvatar(u.getAvatar());
            rankingUserVO.setNickname(u.getNickname());
            rankingUserVO.setPerformanceNum(u.getPerformanceNum());
            rankingUserVO.setRankingPeriod(rankingPeriod);
            rankingUserVO.setUserArt(userArt);
            rankingUserVOList.add(rankingUserVO);
        }
        return Result.newSuccess(rankingUserVOList);
    }
}
