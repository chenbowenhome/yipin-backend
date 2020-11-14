package com.yipin.basic.service;

import VO.PageVO;
import VO.Result;
import args.PageArg;
import com.yipin.basic.VO.ProductionVO;
import com.yipin.basic.VO.UserVO;
import VO.Void;
import com.yipin.basic.entity.user.User;
import com.yipin.basic.form.UserForm;
import com.yipin.basic.form.UserMsgForm;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


public interface UserService {

    /**更新用户信息**/
    Result<Void> updateUserMsg(Integer userId, UserForm userForm);
    /**通过id获取用户信息**/
    Result<UserVO> getUserMsg(Integer id);
    /**生成token**/
    String generateToken(User user);
    /**完善用户信息**/
    Result<Void> completeUserMsg(Integer userId, UserMsgForm userMsgForm);
    /**代表作设置接口**/
    Result<Void> setMainProduction(Integer userId,Integer productionId);
    /**上传图片文件到阿里云服务器，返回图片url**/
    Result<Map<String,String>> uploadImage(MultipartFile file);
    /**生成一个测试用token**/
    Result<Map<String,String>> getTestToken();
    /**获取前三名用户信息**/
    Result<List<UserVO>> findTheTopThree();
    /**关注用户**/
    Result<Void> followUser(Integer userId,Integer followUserId);
    /**取消关注**/
    Result<Void> cancelFollow(Integer userId,Integer followUserId);
    /**获取用户关注列表**/
    Result<PageVO<UserVO>> listFollowUsers(Integer userId, PageArg arg);
    /**获取用户粉丝列表**/
    Result<PageVO<UserVO>> listFanUsers(Integer userId,PageArg arg);
    /**收藏作品**/
    Result<Void> collectProduction(Integer userId,Integer productionId);
    /**取消收藏作品**/
    Result<Void> cancelCollectProduction(Integer userId,Integer productionId);
    /**获取用户收藏的作品**/
    Result<PageVO<ProductionVO>> listCollections(Integer userId,PageArg arg);
    /**将商品加入购物车**/
    Result<String> uploadImageTest(MultipartFile file);
}
