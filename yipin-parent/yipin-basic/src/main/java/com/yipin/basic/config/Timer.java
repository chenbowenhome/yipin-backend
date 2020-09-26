package com.yipin.basic.config;

import com.yipin.basic.dao.ranking.RankingPeriodRepository;
import com.yipin.basic.dao.ranking.RankingUserRepository;
import com.yipin.basic.dao.userDao.UserArtRepository;
import com.yipin.basic.dao.userDao.UserPerformanceRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.ranking.RankingPeriod;
import com.yipin.basic.entity.ranking.RankingUser;
import com.yipin.basic.entity.user.User;
import com.yipin.basic.entity.user.UserArt;
import com.yipin.basic.entity.user.UserPerformance;
import com.yipin.basic.utils.InitUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component //使spring管理
@EnableScheduling //定时任务注解
public class Timer {
    @Autowired
    private UserArtRepository userArtRepository;
    @Autowired
    private UserPerformanceRepository userPerformanceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RankingPeriodRepository rankingPeriodRepository;
    @Autowired
    private RankingUserRepository rankingUserRepository;

    /**更新用户艺能**/
    @Scheduled(cron = "0 59 23 * * ?")
    public void updateUserArt(){
        List<Integer> idList = userRepository.findUserId();
        for (Integer id : idList) {
            UserArt userArt = userArtRepository.findLastUserArt(id);
            if (userArt != null){
                Integer num = userArt.getOnlineMinute() / (userArt.getUpgradeHours() * 60);
                Integer  onlineMinute = userArt.getOnlineMinute();
                Integer cherry = userArt.getCherry();
                Integer strawberry = userArt.getStrawberry();
                Integer apple = userArt.getApple();
                if (num > 0){
                    cherry = cherry + num;
                    onlineMinute = onlineMinute - num * userArt.getUpgradeHours() * 60;
                    userArt.setAllOnlineHours(userArt.getAllOnlineHours() + num);
                }
                if (cherry / 4 > 0){
                    strawberry = strawberry + cherry / 4;
                    cherry = cherry - (cherry / 4) * 4;
                }
                if (strawberry / 4 > 0){
                    apple = apple + strawberry / 4;
                    strawberry = strawberry - (strawberry / 4) * 4;
                }
                userArt.setId(null);
                userArt.setOnlineMinute(onlineMinute);
                userArt.setCherry(cherry);
                userArt.setStrawberry(strawberry);
                userArt.setApple(apple);
                userArt.setCreateTime(new Date());
                userArt.setUpdateTime(new Date());
                userArtRepository.save(userArt);
            }
        }
    }

    /**更新用户品值**/
    @Scheduled(cron = "0 58 23 ? * SUN")
    public void updateUserPerformance(){
        List<Integer> idList = userRepository.findUserId();
        for (Integer id : idList) {
            UserPerformance userPerformance = userPerformanceRepository.findLastUserPerformance(id);
            User user = userRepository.findUserById(id);
            double insistence = 0.00;
            double assiduous = 0.00;
            double participation = 0.00;
            double worth = 0.00;
            //坚持度
            if (userPerformance.getUploadProductionNums() == 0){
                insistence = -0.10;
            }else{
                insistence = 0.10;
            }
            //勤奋度
            if (userPerformance.getUploadProductionNums() == 1){
                assiduous = 0.30;
            }else if (userPerformance.getUploadProductionNums() == 2){
                assiduous = 0.70;
            }else if (userPerformance.getUploadProductionNums() >= 3){
                assiduous = 1.00;
            }
            //参与度
            Integer maxComment = userPerformanceRepository.findMaxCommentNums();
            Integer maxLike = userPerformanceRepository.findMaxLikeNums();
            if (maxComment != 0){
                participation = participation + 0.5 * (userPerformance.getCommentNums() / maxComment);
            }
            if (maxLike != 0){
                participation = participation + 0.5 * (userPerformance.getLikeNums() / maxLike);
            }
            //价值度
            double maxException = userPerformanceRepository.findMaxExceptionalAmount();
            double maxReward = userPerformanceRepository.findMaxRewardAmount();
            double exceptionAmount = userPerformance.getExceptionalAmount().doubleValue();
            double rewardAmount = userPerformance.getRewardAmount().doubleValue();
            if (maxException != 0){
                worth = worth + 0.7 * (exceptionAmount / maxException);
            }
            if (maxReward != 0){
                worth = worth + 0.3 * (rewardAmount / maxReward);
            }

            double res = 0.25 * insistence + 0.25 * assiduous + 0.25 * participation + 0.25 * worth;
            if (res < 0){
                user.setPerformanceNum(new BigDecimal(0));
            }else{
                user.setPerformanceNum(new BigDecimal(res));
            }
            userRepository.save(user);
            userPerformance.setIsPeriod(0);
            userPerformance.setInsistence(new BigDecimal(insistence));
            userPerformance.setAssiduous(new BigDecimal(assiduous));
            userPerformance.setParticipation(new BigDecimal(participation));
            userPerformance.setWorth(new BigDecimal(worth));
            UserPerformance u = new UserPerformance();
            InitUtils.InitUserPerformance(u);
            u.setUserId(id);
            userPerformanceRepository.save(userPerformance);
            userPerformanceRepository.save(u);
        }
    }

    /**更新用户品值排名**/
    @Scheduled(cron = "0 00 00 ? * MON")
    public void updateUserPerformanceOrder(){
        List<User> userList = userRepository.findUserByOrderByPerformanceNumDesc();
        RankingPeriod rankingPeriod = rankingPeriodRepository.findLastRankingPeriod();
        if (rankingPeriod == null){//如果为空就新建一个期数
            RankingPeriod rp = new RankingPeriod();
            rp.setPeriod(1);
            Date startDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE,7);
            Date endDate = calendar.getTime();
            rp.setStartTime(startDate);
            rp.setEndTime(endDate);
            rankingPeriodRepository.save(rp);
            rankingPeriod = rp;
        }else {
            RankingPeriod rp = new RankingPeriod();
            rp.setPeriod(rankingPeriod.getPeriod() + 1);
            Date startDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE,7);
            Date endDate = calendar.getTime();
            rp.setStartTime(startDate);
            rp.setEndTime(endDate);
            rankingPeriodRepository.save(rp);
            rankingPeriod = rp;
        }
        int index = 1;
        for (User user : userList) {
            user.setRanking(index);
            RankingUser rankingUser = new RankingUser();
            rankingUser.setUserId(user.getId());
            rankingUser.setPeriod(rankingPeriod.getPeriod());
            rankingUser.setCreateTime(new Date());
            rankingUser.setRanking(index);
            userRepository.save(user);
            rankingUserRepository.save(rankingUser);
            index++;
        }
    }
}
