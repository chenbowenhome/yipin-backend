package com.yipin.basic.config;

import com.yipin.basic.dao.userDao.UserArtRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.user.UserArt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component //使spring管理
@EnableScheduling //定时任务注解
public class Timer {
    @Autowired
    private UserArtRepository userArtRepository;
    @Autowired
    private UserRepository userRepository;

    /**更新用户艺能**/
    @Scheduled(cron = "0 35 20 * * ?")
    public void updateUserArt(){
        List<Integer> idList = userRepository.findUserId();
        for (Integer id : idList) {
            UserArt userArt = userArtRepository.findLastUserArt(id);
            if (userArt != null){
                Integer num = userArt.getOnlineHours() / userArt.getUpgradeHours();
                Integer  onlineHours = userArt.getOnlineHours();
                Integer cherry = userArt.getCherry();
                Integer strawberry = userArt.getStrawberry();
                Integer apple = userArt.getApple();
                if (num > 0){
                    cherry = cherry + num;
                    onlineHours = onlineHours - num * userArt.getUpgradeHours();
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
                userArt.setOnlineHours(onlineHours);
                userArt.setCherry(cherry);
                userArt.setStrawberry(strawberry);
                userArt.setApple(apple);
                userArt.setCreateTime(new Date());
                userArt.setUpdateTime(new Date());
                userArtRepository.save(userArt);
            }
        }
    }

}
