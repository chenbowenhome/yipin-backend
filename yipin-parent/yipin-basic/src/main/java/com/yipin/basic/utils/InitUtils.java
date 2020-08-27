package com.yipin.basic.utils;

import com.yipin.basic.entity.user.User;
import com.yipin.basic.entity.user.UserArt;
import com.yipin.basic.entity.user.UserPerformance;

import java.math.BigDecimal;
import java.util.Date;

public class InitUtils {


    /**初始化用户**/
    public static User InitUser(User user){
        user.setRegdate(new Date());
        user.setLastdate(new Date());
        user.setUpdatetime(new Date());
        user.setSex(0);
        user.setFanscount(0);
        user.setFollowcount(0);
        user.setLikes(0);
        user.setGifts(0);
        user.setAssessments(0);
        user.setInformationStatus(0);
        user.setPerformanceNum(new BigDecimal(0.00));
        return user;
    }

    /**初始化用户艺能参数**/
    public static UserArt InitUserArt(UserArt userArt){
        userArt.setCherry(0);
        userArt.setStrawberry(0);
        userArt.setApple(0);
        userArt.setOnlineHours(0);
        userArt.setAllOnlineHours(0);
        userArt.setUpgradeHours(1);//TODO 目前设置为一个小时，用于测试
        userArt.setCreateTime(new Date());
        userArt.setUpdateTime(new Date());
        return userArt;
    }

    /**初始化用户品值参数**/
    public static UserPerformance InitUserPerformance(UserPerformance userPerformance){
        userPerformance.setAllUploadNums(0);
        userPerformance.setUploadProductionNums(0);
        userPerformance.setPeriodDays(7);//TODO 目前设置为7天
        userPerformance.setCommentNums(0);
        userPerformance.setBeCommentedNums(0);
        userPerformance.setExceptionalAmount(new BigDecimal(0.0));
        userPerformance.setRewardAmount(new BigDecimal(0.0));
        userPerformance.setInsistence(new BigDecimal(0.0));
        userPerformance.setAssiduous(new BigDecimal(0.0));
        userPerformance.setParticipation(new BigDecimal(0.0));
        userPerformance.setWorth(new BigDecimal(0.0));
        userPerformance.setCreateTime(new Date());
        userPerformance.setUpdateTime(new Date());
        return userPerformance;
    }

}
