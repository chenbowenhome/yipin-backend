package com.yipin.basic.controller;

import VO.PageVO;
import VO.Result;
import args.PageArg;
import com.yipin.basic.VO.ProductionVO;
import com.yipin.basic.VO.RankingUserVO;
import com.yipin.basic.VO.RankingVO;
import com.yipin.basic.VO.UserVO;
import VO.Void;
import args.data.AuthCode2Session;
import args.data.WxUserInfo;
import com.yipin.basic.config.WechatMiniprogramConfig;
import com.yipin.basic.dao.ranking.RankingUserRepository;
import com.yipin.basic.dao.userDao.UserArtRepository;
import com.yipin.basic.dao.userDao.UserMoneyRepository;
import com.yipin.basic.dao.userDao.UserPerformanceRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.ranking.RankingPeriod;
import com.yipin.basic.entity.ranking.RankingUser;
import com.yipin.basic.entity.user.User;
import com.yipin.basic.entity.user.UserArt;
import com.yipin.basic.entity.user.UserMoney;
import com.yipin.basic.entity.user.UserPerformance;
import com.yipin.basic.form.UserForm;
import args.WxLoginArg;
import com.yipin.basic.form.UserMsgForm;
import com.yipin.basic.service.RankingService;
import com.yipin.basic.service.UserService;
import com.yipin.basic.utils.InitUtils;
import enums.ResultEnum;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = "用户相关接口(个人页面)")
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
    private UserMoneyRepository userMoneyRepository;
    @Autowired
    private UserPerformanceRepository userPerformanceRepository;
    @Autowired
    private RankingService rankingService;

    /**更新用户信息**/
    @ApiOperation("更新用户信息,传几个更新几个(修改用户信息)")
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
    public Result<UserVO> login(@Valid @RequestBody WxLoginArg wxLoginArg,@RequestParam(required = false) String phone,
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
            if (phone != null){
                user.setMobile(phone);
            }
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
            //创建用户未币表
            UserMoney userMoney = new UserMoney();
            userMoney.setUserId(user.getId());
            userMoney.setMoney(new BigDecimal(0.0));
            userMoney.setUpdateTime(new Date());

            userMoneyRepository.save(userMoney);
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
    @ApiOperation(value = "完善用户信息(新注册的用户需要完善用户的信息)")
    @PostMapping("/completeUserMsg")
    public Result<Void> completeUserMsg(Integer userId,@Valid @RequestBody UserMsgForm userMsgForm){
        return userService.completeUserMsg(userId,userMsgForm);
    }

    /**代表作设置接口**/
    @ApiOperation(value = "代表作设置接口(设置用户的代表作)", notes = "传用户id和被设置为代表作的作品id")
    @PostMapping("/setMainProduction")
    public Result<Void> setMainProduction(Integer userId,Integer productionId){
        return userService.setMainProduction(userId,productionId);
    }

    /**上传图片文件到阿里云服务器，返回图片url**/
    @ApiOperation("上传图片到服务器，返回图片url,最多10MB,图片格式最好为png，jpg")
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
    @ApiOperation("获取前三名用户信息(首页获取前三名用户的信息)")
    @RequestMapping(value = "/findTheTopThree",method = RequestMethod.GET)
    public Result<List<UserVO>> findTheTopThree() {
        return userService.findTheTopThree();
    }

    /**关注用户**/
    @ApiOperation("关注用户(传入用户id与被关注用户的id)")
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
    @ApiOperation("获取用户的关注列表")
    @RequestMapping(value = "/listFollowUsers",method = RequestMethod.POST)
    public Result<PageVO<UserVO>> listFollowUsers(Integer userId,@RequestBody PageArg arg){
        arg.validate();
        return userService.listFollowUsers(userId,arg);
    }

    /**获取用户粉丝列表**/
    @ApiOperation("获取用户的粉丝列表")
    @RequestMapping(value = "/listFanUsers",method = RequestMethod.POST)
    public Result<PageVO<UserVO>> listFanUsers(Integer userId,@RequestBody PageArg arg){
        arg.validate();
        return userService.listFanUsers(userId,arg);
    }

    /**上传图片测试**/
    @ApiOperation("上传图片测试（测试接口，非前台接口）")
    @RequestMapping(value = "/uploadImageTest",method = RequestMethod.POST)
    public Result<String> uploadImageTest(@RequestParam("file") MultipartFile file){
        return userService.uploadImageTest(file);
    }

    /**获取排名前20名用户**/
    @ApiOperation("获取排名前20名用户(传入当前期数)")
    @RequestMapping(value = "/findTheRanking",method = RequestMethod.GET)
    public Result<RankingVO> findTheRanking(Integer period) {
        return rankingService.findTheRanking(period);
    }

    /**获取所有期数**/
    @ApiOperation("获取所有期数(就是该排名目前进行到了第几期)")
    @RequestMapping(value = "/findAllPeriod",method = RequestMethod.GET)
    public Result<List<RankingPeriod>> findAllPeriod(){
        return rankingService.findAllPeriod();
    }

    /**获取目标期数用户前后五名**/
    @ApiOperation("获取目标期数用户前后五名")
    @RequestMapping(value = "/findUserRanking",method = RequestMethod.GET)
    public Result<List<RankingUserVO>> findUserRanking(Integer userId, Integer period){
        return rankingService.findUserRanking(userId,period);
    }

    /**获取用户参加的期数**/
    @ApiOperation("获取用户参加的期数")
    @RequestMapping(value = "/getUserPeriod",method = RequestMethod.GET)
    public Result<List<Integer>> getUserPeriod(Integer userId) {
        return rankingService.getUserPeriod(userId);
    }

    /**记录开始时间**/
    @ApiOperation("记录开始时间，用户进入小程序后调用此接口")
    @RequestMapping(value = "/startTime",method = RequestMethod.POST)
    public Result<Void> startTime(Integer userId){
        if (userId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Date date = new Date();
        UserArt userArt = userArtRepository.findLastUserArt(userId);
        userArt.setStartTime(date);
        userArtRepository.save(userArt);
        return Result.newSuccess();
    }

    /**记录结束时间**/
    @ApiOperation("记录结束时间，用户离开小程序后调用此接口")
    @RequestMapping(value = "/endTime",method = RequestMethod.POST)
    public Result<Void> endTime(Integer userId){
        if (userId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Date endTime = new Date();
        UserArt userArt = userArtRepository.findLastUserArt(userId);
        userArt.setEndTime(endTime);
        Date startTime = userArt.getStartTime();
        int day = endTime.getDay() - startTime.getDay();
        int minutes = endTime.getMinutes() - startTime.getMinutes();
        int allMinutes = day * 60 + minutes;
        if (allMinutes > 0 && allMinutes <= 5) {
            userArt.setAllOnlineHours(userArt.getAllOnlineHours() + allMinutes);
            userArt.setOnlineMinute(userArt.getOnlineMinute() + allMinutes);
        }
        userArtRepository.save(userArt);
        return Result.newSuccess();
    }

    /**收藏作品**/
    @ApiOperation("收藏作品")
    @RequestMapping(value = "/collectProduction",method = RequestMethod.POST)
    Result<Void> collectProduction(Integer userId,Integer productionId){
        return userService.collectProduction(userId,productionId);
    }
    /**取消收藏作品**/
    @ApiOperation("取消收藏作品")
    @RequestMapping(value = "/cancelCollectProduction",method = RequestMethod.POST)
    Result<Void> cancelCollectProduction(Integer userId,Integer productionId){
        return userService.cancelCollectProduction(userId,productionId);
    }
    /**获取用户收藏的作品**/
    @ApiOperation("获取用户收藏的作品")
    @RequestMapping(value = "/listCollections",method = RequestMethod.POST)
    Result<PageVO<ProductionVO>> listCollections(Integer userId,@RequestBody PageArg arg){
        arg.validate();
        return userService.listCollections(userId,arg);
    }

    /**获取用户未币信息**/
    @ApiOperation("获取用户未币信息")
    @RequestMapping(value = "/getUserMoney",method = RequestMethod.GET)
    Result<UserMoney> getUserMoney(Integer userId){
        if (userId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        UserMoney userMoney = userMoneyRepository.findUserMoneyByUserId(userId);
        return Result.newSuccess(userMoney);
    }

}
