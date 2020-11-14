package com.yipin.basic.controller;

import VO.Result;
import VO.Void;
import com.yipin.basic.dao.othersDao.ArtActivityTranspondRepository;
import com.yipin.basic.dao.userDao.UserMoneyRepository;
import com.yipin.basic.entity.others.ArtActivityTranspond;
import com.yipin.basic.entity.user.UserMoney;
import enums.ResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.Date;

@Api(tags = "分享相关接口(活动分享页面)")
@RestController
@RequestMapping("/share")
public class ShareController {

    @Autowired
    private ArtActivityTranspondRepository artActivityTranspondRepository;
    @Autowired
    private UserMoneyRepository userMoneyRepository;

    /**创建一个分享**/
    @ApiOperation("创建一个分享(当第一个用户分享后调用此接口，传入用户id)")
    @RequestMapping(value = "/createShare",method = RequestMethod.POST)
    public Result<Void> createShare(Integer userId){
        if (userId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        ArtActivityTranspond artActivityTranspond = new ArtActivityTranspond();
        artActivityTranspond.setFirstMoney(new BigDecimal(3));
        artActivityTranspond.setSecondMoney(new BigDecimal(1));
        artActivityTranspond.setFirstUserId(userId);
        artActivityTranspond.setCreateTime(new Date());
        artActivityTranspond.setUpdateTime(new Date());
        artActivityTranspond.setIsEnd(0);
        artActivityTranspondRepository.save(artActivityTranspond);
        return Result.newSuccess();
    }

    /**用户接收此分享并且在活动页面下单**/
    @ApiOperation("用户接收此分享并且在活动页面下单(当第二个用户接收到第一个用户的分享链接并且购买物品后调用此接口，传入分享id和用户自己的id)")
    @RequestMapping(value = "/finishShare",method = RequestMethod.POST)
    public Result<Void> finishShare(Integer shareId,Integer userId){
        if (shareId == null || userId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        ArtActivityTranspond artActivityTranspond = artActivityTranspondRepository.findArtActivityTranspondById(shareId);
        if (artActivityTranspond == null || artActivityTranspond.getIsEnd() == 1){
            return Result.newError("分享链接不存在或者已完结");
        }
        if (artActivityTranspond.getFirstUserId() == userId){
            return Result.newError("不能分享给自己");
        }
        if (artActivityTranspond.getFirstUserId() != null && artActivityTranspond.getSecondUserId() == null){
            UserMoney userMoney = userMoneyRepository.findUserMoneyByUserId(artActivityTranspond.getFirstUserId());
            userMoney.setMoney(new BigDecimal(userMoney.getMoney().doubleValue() + artActivityTranspond.getFirstMoney().doubleValue()));
            userMoneyRepository.save(userMoney);
            artActivityTranspond.setSecondUserId(userId);
        }
        if (artActivityTranspond.getFirstUserId() != null && artActivityTranspond.getSecondUserId() != null){
            UserMoney user1Money = userMoneyRepository.findUserMoneyByUserId(artActivityTranspond.getFirstUserId());
            UserMoney user2Money = userMoneyRepository.findUserMoneyByUserId(artActivityTranspond.getSecondUserId());
            user1Money.setMoney(new BigDecimal(user1Money.getMoney().doubleValue() + artActivityTranspond.getSecondMoney().doubleValue()));
            user2Money.setMoney(new BigDecimal(user1Money.getMoney().doubleValue() + artActivityTranspond.getFirstMoney().doubleValue()));
            userMoneyRepository.save(user1Money);
            userMoneyRepository.save(user2Money);
            artActivityTranspond.setIsEnd(1);
        }
        artActivityTranspondRepository.save(artActivityTranspond);
        return Result.newSuccess();
    }


}
