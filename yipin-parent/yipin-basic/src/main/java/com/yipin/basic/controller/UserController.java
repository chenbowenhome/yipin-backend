package com.yipin.basic.controller;

import VO.PageVO;
import VO.Result;
import args.PageArg;
import com.yipin.basic.VO.RankingUserVO;
import com.yipin.basic.VO.RankingVO;
import com.yipin.basic.VO.UserVO;
import VO.Void;
import args.data.AuthCode2Session;
import args.data.WxUserInfo;
import com.yipin.basic.config.WechatMiniprogramConfig;
import com.yipin.basic.dao.ranking.RankingUserRepository;
import com.yipin.basic.dao.userDao.UserArtRepository;
import com.yipin.basic.dao.userDao.UserPerformanceRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.ranking.RankingPeriod;
import com.yipin.basic.entity.ranking.RankingUser;
import com.yipin.basic.entity.user.User;
import com.yipin.basic.entity.user.UserArt;
import com.yipin.basic.entity.user.UserPerformance;
import com.yipin.basic.form.UserForm;
import args.WxLoginArg;
import com.yipin.basic.form.UserMsgForm;
import com.yipin.basic.service.RankingService;
import com.yipin.basic.service.UserService;
import com.yipin.basic.utils.InitUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utils.JSONUtils;
import utils.OkHttpUtil;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserArtRepository userArtRepository;
    @Autowired
    private UserPerformanceRepository userPerformanceRepository;
    @Autowired
    private RankingService rankingService;

    /**更新用户信息**/
    @ApiOperation("更新用户信息,传几个更新几个")
    @RequestMapping(value = "/updateUserMsg",method = RequestMethod.POST)
    public Result<Void> updateUserMsg(Integer userId, @RequestBody UserForm userForm){
        return userService.updateUserMsg(userId,userForm);
    }

    /**通过id获取用户信息**/
    @ApiOperation("通过id获取用户信息")
    @RequestMapping(value = "/getUserMsg",method = RequestMethod.POST)
    public Result<UserVO> getUserMsg(Integer id){
        return userService.getUserMsg(id);
    }

    /**小程序登陆注册接口**/
    @ApiOperation(value = "小程序登陆注册接口", notes = "该接口返回 token")
    @PostMapping("/wxlogin")
    public Result<UserVO> login(@Valid @RequestBody WxLoginArg wxLoginArg,
                                    HttpServletResponse response) {
        // 小程序登陆
        // auth.code2Session 地址
        String url = String.format(
                " https://api.weixin.qq.com/sns/jscode2session?" +
                        "appid=%s&" + // appid
                        "secret=%s&" + // secret
                        "js_code=%s&" + // code
                        "grant_type=authorization_code",
                WechatMiniprogramConfig.APPID, WechatMiniprogramConfig.SECRET, wxLoginArg.getCode()
        );

        String jsonData = OkHttpUtil.doGet(url);
        AuthCode2Session authCode2Session = JSONUtils.jsonToPojo(jsonData, AuthCode2Session.class);
        // 验证 auth.code2Session 请求是否发送成功，0 表示成功，详见小程序官方文档
        if (authCode2Session == null || authCode2Session.getErrcode() != 0) {
            return Result.newError("服务端请求 auth.code2Session 接口错误");
        }
        // 根据  openid 判断该用户是否存在
        User queryUser = userRepository.findUserByOpenid(authCode2Session.getOpenid());
        String jwtToken = "";
        UserArt queryUserArt = null;
        UserPerformance queryUserPerformance = null;
        if (queryUser != null) {
            // 用户存在，通过 id，openid 生成 jwtToken
            jwtToken = userService.generateToken(queryUser);
        } else {
            // 用户不存在，进行保存
            // 从表单获取用户数据 nickname,gender,language,city,province,country,avatarUrl,
            WxUserInfo wxUserInfo = JSONUtils.jsonToPojo(wxLoginArg.getRawData(), WxUserInfo.class);
            if (wxUserInfo == null) {
                return Result.newError("rawData 错误,用户信息不能为空");
            }
            User user = new User();
            InitUtils.InitUser(user);
            user.setOpenid(authCode2Session.getOpenid());
            user.setNickname(wxUserInfo.getNickname());
            user.setAvatar(wxUserInfo.getAvatarUrl());
            userRepository.save(user);

            // 通过 id，openid 生成 jwtToken
            jwtToken = userService.generateToken(user);
            queryUser = user;
            //创建用户的艺能与品值参数
            UserArt userArt = new UserArt();
            UserPerformance userPerformance = new UserPerformance();
            InitUtils.InitUserArt(userArt);
            userArt.setUserId(queryUser.getId());
            InitUtils.InitUserPerformance(userPerformance);
            userPerformance.setUserId(queryUser.getId());
            queryUserPerformance = userPerformanceRepository.save(userPerformance);
            queryUserArt = userArtRepository.save(userArt);
        }
        WxUserInfo wxUserInfo = JSONUtils.jsonToPojo(wxLoginArg.getRawData(), WxUserInfo.class);
        response.setHeader("token", jwtToken);
        UserVO userInfoVo = new UserVO();
        if (queryUserArt == null && queryUserPerformance == null){
            queryUserArt = userArtRepository.findLastUserArt(queryUser.getId());
            queryUserPerformance = userPerformanceRepository.findLastUserPerformance(queryUser.getId());
        }
        BeanUtils.copyProperties(queryUser, userInfoVo);
        userInfoVo.setAvatar(wxUserInfo.getAvatarUrl());
        userInfoVo.setUserArt(queryUserArt);
        userInfoVo.setUserPerformance(queryUserPerformance);
        return Result.newSuccess(userInfoVo);
    }

    /**完善用户信息**/
    @ApiOperation(value = "完善用户信息")
    @PostMapping("/completeUserMsg")
    public Result<Void> completeUserMsg(Integer userId,@Valid @RequestBody UserMsgForm userMsgForm){
        return userService.completeUserMsg(userId,userMsgForm);
    }

    /**代表作设置接口**/
    @ApiOperation(value = "代表作设置接口", notes = "传用户id和被设置为代表作的作品id")
    @PostMapping("/setMainProduction")
    public Result<Void> setMainProduction(Integer userId,Integer productionId){
        return userService.setMainProduction(userId,productionId);
    }

    /**上传图片文件到阿里云服务器，返回图片url**/
    @ApiOperation("上传图片文件到阿里云服务器，返回图片url,最多2MB,图片格式最好为png，jpg")
    @RequestMapping(value = "/uploadImage",method = RequestMethod.POST)
    public Result<Map<String,String>> uploadImage(@RequestParam("file") MultipartFile file){
        return userService.uploadImage(file);
    }

    @ApiOperation("获取测试用token，swagger可以用右上角的Authorize登录，返回测试用token和测试用用户id")
    @RequestMapping(value = "/getTestToken",method = RequestMethod.GET)
    public Result<Map<String, String>> getTestToken() {
        return userService.getTestToken();
    }

    /**获取前三名用户信息**/
    @ApiOperation("获取前三名用户信息")
    @RequestMapping(value = "/findTheTopThree",method = RequestMethod.GET)
    public Result<List<UserVO>> findTheTopThree() {
        return userService.findTheTopThree();
    }

    /**关注用户**/
    @ApiOperation("关注用户")
    @RequestMapping(value = "/followUser",method = RequestMethod.POST)
    public Result<Void> followUser(Integer userId,Integer followUserId){
        return userService.followUser(userId,followUserId);
    }

    /**取消关注**/
    @ApiOperation("取消关注")
    @RequestMapping(value = "/cancelFollow",method = RequestMethod.POST)
    public Result<Void> cancelFollow(Integer userId,Integer followUserId){
        return userService.cancelFollow(userId,followUserId);
    }

    /**获取用户关注列表**/
    @ApiOperation("获取用户关注列表")
    @RequestMapping(value = "/listFollowUsers",method = RequestMethod.POST)
    public Result<PageVO<UserVO>> listFollowUsers(Integer userId,@RequestBody PageArg arg){
        arg.validate();
        return userService.listFollowUsers(userId,arg);
    }

    /**获取用户粉丝列表**/
    @ApiOperation("获取用户粉丝列表")
    @RequestMapping(value = "/listFanUsers",method = RequestMethod.POST)
    public Result<PageVO<UserVO>> listFanUsers(Integer userId,@RequestBody PageArg arg){
        arg.validate();
        return userService.listFanUsers(userId,arg);
    }

    /**上传图片测试**/
    @ApiOperation("上传图片测试")
    @RequestMapping(value = "/uploadImageTest",method = RequestMethod.POST)
    public Result<String> uploadImageTest(@RequestParam("file") MultipartFile file){
        return userService.uploadImageTest(file);
    }

    /**获取排名前20名用户**/
    @ApiOperation("获取排名前20名用户")
    @RequestMapping(value = "/findTheRanking",method = RequestMethod.GET)
    public Result<RankingVO> findTheRanking(Integer period) {
        return rankingService.findTheRanking(period);
    }

    /**获取所有期数**/
    @ApiOperation("获取所有期数")
    @RequestMapping(value = "/findAllPeriod",method = RequestMethod.GET)
    public Result<List<RankingPeriod>> findAllPeriod(){
        return rankingService.findAllPeriod();
    }

    /**获取目标期数用户前后五名**/
    @ApiOperation("获取目标期数用户前后五名")
    @RequestMapping(value = "/findUserRanking",method = RequestMethod.GET)
    public Result<List<RankingUser>> findUserRanking(Integer userId, Integer period){
        return rankingService.findUserRanking(userId,period);
    }
}
