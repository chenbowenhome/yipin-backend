package com.yipin.basic.service.impl;

import VO.PageVO;
import VO.Result;
import args.PageArg;
import com.yipin.basic.VO.UserVO;
import VO.Void;
import com.yipin.basic.utils.aliyunOss.AliyunOSSUtil;
import com.yipin.basic.dao.productionDao.ProductionRepository;
import com.yipin.basic.dao.userDao.FollowRepository;
import com.yipin.basic.dao.userDao.UserArtRepository;
import com.yipin.basic.dao.userDao.UserPerformanceRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.production.Production;
import com.yipin.basic.entity.user.Follow;
import com.yipin.basic.entity.user.User;
import com.yipin.basic.entity.user.UserArt;
import com.yipin.basic.entity.user.UserPerformance;
import com.yipin.basic.form.UserForm;
import com.yipin.basic.form.UserMsgForm;
import com.yipin.basic.service.UserService;
import enums.ResultEnum;
import enums.RoleEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductionRepository productionRepository;
    @Autowired
    private UserArtRepository userArtRepository;
    @Autowired
    private UserPerformanceRepository userPerformanceRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private HttpServletRequest request;
    //阿里云上传图片工具类
    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    /**
     * 更新用户信息
     **/
    @Override
    public Result<Void> updateUserMsg(Integer userId, UserForm userForm) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)) {
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (userForm == null || userId == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        User user = userRepository.findUserById(userId);
        if (user == null) {
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        if (userForm.getAvatar() != null) {
            user.setAvatar(userForm.getAvatar());
        }
        if (userForm.getEmail() != null) {
            user.setEmail(userForm.getEmail());
        }
        if (userForm.getNickname() != null) {
            user.setNickname(userForm.getNickname());
        }
        if (userForm.getSex() != null) {
            user.setSex(userForm.getSex());
        }
        if (userForm.getDescription() != null) {
            user.setDescription(userForm.getDescription());
        }
        if (userForm.getMobile() != null) {
            user.setMobile(userForm.getMobile());
        }
        if (userForm.getUnit() != null) {
            user.setUnit(userForm.getUnit());
        }
        userRepository.save(user);
        return Result.newSuccess();
    }

    /**
     * 通过id获取用户信息
     **/
    @Override
    public Result<UserVO> getUserMsg(Integer id) {
        if (id == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        User user = userRepository.findUserById(id);
        if (user == null) {
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return Result.newSuccess(userVO);
    }

    /**
     * 生成token
     **/
    @Override
    public String generateToken(User user) {
        String token = null;
        if (user.getIdentity() == null) {
            token = jwtUtil.createJWT(user.getId(), user.getOpenid(), RoleEnum.VISITOR.getRole());
        } else if (user.getIdentity() == 0) {
            token = jwtUtil.createJWT(user.getId(), user.getOpenid(), RoleEnum.STUDENT.getRole());
        } else if (user.getIdentity() == 1) {
            token = jwtUtil.createJWT(user.getId(), user.getOpenid(), RoleEnum.TEACHER.getRole());
        }
        return token;
    }

    /**
     * 完善用户信息
     **/
    @Override
    public Result<Void> completeUserMsg(Integer userId, UserMsgForm userMsgForm) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)) {
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        User user = userRepository.findUserById(userId);
        if (user == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        BeanUtils.copyProperties(userMsgForm, user);
        user.setInformationStatus(1);
        user.setUpdatetime(new Date());
        userRepository.save(user);
        return Result.newSuccess();
    }

    /**
     * 代表作设置接口
     **/
    @Override
    public Result<Void> setMainProduction(Integer userId, Integer productionId) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)) {
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (productionId == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Production production = productionRepository.findProductionById(productionId);
        User user = userRepository.findUserById(userId);
        if (production == null) {
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        if (user == null) {
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        if (production.getCheckStatus() == 0 || production.getEvaluateStatus() == 0 || production.getPublishStatus() == 0) {
            return Result.newResult(ResultEnum.PRODUCTION_ERROR);
        }
        if (user.getMainProductionId() != null) {
            Production production1 = productionRepository.findProductionById(user.getMainProductionId());
            production1.setIsMainProduction(0);
            productionRepository.save(production1);
        }
        user.setMainProductionId(productionId);
        production.setIsMainProduction(1);
        userRepository.save(user);
        productionRepository.save(production);
        return Result.newSuccess();
    }

    /**
     * 上传图片文件到阿里云服务器，返回图片url
     **/
    @Override
    public Result<Map<String, String>> uploadImage(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String uploadUrl = null;
        if (file == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        try {
            if (file != null) {
                if (!"".equals(filename.trim())) {
                    File newFile = new File(filename);
                    FileOutputStream os = new FileOutputStream(newFile);
                    os.write(file.getBytes());
                    os.close();
                    file.transferTo(newFile);
                    // 上传到OSS
                    uploadUrl = aliyunOSSUtil.upLoad(newFile);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("imageUrl", uploadUrl);
        return Result.newSuccess(map);
    }

    @Override
    public Result<Map<String, String>> getTestToken() {
        String token = jwtUtil.createJWT(2, "oAjfV5O15F9txJYm-QYpMimo2ghM", RoleEnum.STUDENT.getRole());
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("userId", "2");
        return Result.newSuccess(map);
    }

    /**
     * 获取前三名用户信息
     **/
    @Override
    public Result<List<UserVO>> findTheTopThree() {
        List<User> userList = userRepository.findTheTopThree();
        if (userList.isEmpty()) {
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            UserArt userArt = userArtRepository.findLastUserArt(user.getId());
            UserPerformance userPerformance = userPerformanceRepository.findLastUserPerformance(user.getId());
            userVO.setUserArt(userArt);
            userVO.setUserPerformance(userPerformance);
            userVOList.add(userVO);
        }
        return Result.newSuccess(userVOList);
    }

    /**
     * 关注用户
     **/
    @Override
    public Result<Void> followUser(Integer userId, Integer followUserId) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)) {
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (userId == null || followUserId == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Follow f = followRepository.findFollowByUserIdAndFollowUserId(userId, followUserId);
        if (f != null) {
            return Result.newError("已关注该用户，无需重复关注");
        }
        User user = userRepository.findUserById(userId);
        User fan = userRepository.findUserById(followUserId);
        if (user == null || fan == null) {
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        user.setFollowcount(user.getFollowcount() + 1);
        fan.setFanscount(fan.getFanscount() + 1);
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowUserId(followUserId);
        follow.setCreateTime(new Date());
        followRepository.save(follow);
        return Result.newSuccess();
    }

    /**
     * 取消关注
     **/
    @Override
    public Result<Void> cancelFollow(Integer userId, Integer followUserId) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)) {
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (userId == null || followUserId == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Follow follow = followRepository.findFollowByUserIdAndFollowUserId(userId, followUserId);
        if (follow == null) {
            return Result.newError("未关注该用户，无法取消关注");
        }
        User user = userRepository.findUserById(userId);
        User fan = userRepository.findUserById(followUserId);
        if (user == null || fan == null) {
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        user.setFollowcount(user.getFollowcount() - 1);
        fan.setFanscount(fan.getFanscount() - 1);
        followRepository.delete(follow);
        return Result.newSuccess();
    }

    /**
     * 获取用户关注列表
     **/
    @Override
    public Result<PageVO<UserVO>> listFollowUsers(Integer userId, PageArg arg) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)) {
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }
        if (userId == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }

        Pageable pageable = PageRequest.of(arg.getPageNo() - 1, arg.getPageSize());
        Page<Follow> followPage = followRepository.findFollowByUserIdOrderByCreateTimeDesc(userId, pageable);
        List<Follow> followList = followPage.getContent();
        List<UserVO> userVOList = new ArrayList<>();
        for (Follow follow : followList) {
            UserVO userVO = new UserVO();
            User user = userRepository.findUserById(follow.getFollowUserId());
            BeanUtils.copyProperties(user, userVO);
            userVOList.add(userVO);
        }

        //构建pageVo对象
        PageVO<UserVO> pageVo = PageVO.<UserVO>builder()
                .totalPage(followPage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(userVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**
     * 获取用户粉丝列表
     **/
    @Override
    public Result<PageVO<UserVO>> listFanUsers(Integer userId, PageArg arg) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)) {
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }
        if (userId == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }

        Pageable pageable = PageRequest.of(arg.getPageNo() - 1, arg.getPageSize());
        Page<Follow> followPage = followRepository.findFollowByFollowUserIdOrderByCreateTimeDesc(userId, pageable);
        List<Follow> followList = followPage.getContent();
        List<UserVO> userVOList = new ArrayList<>();
        for (Follow follow : followList) {
            UserVO userVO = new UserVO();
            User user = userRepository.findUserById(follow.getUserId());
            BeanUtils.copyProperties(user, userVO);
            userVOList.add(userVO);
        }

        //构建pageVo对象
        PageVO<UserVO> pageVo = PageVO.<UserVO>builder()
                .totalPage(followPage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(userVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    @Override
    public Result<String> uploadImageTest(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (file == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        if (file != null) {
            if (!"".equals(filename.trim())) {
                File newFile = new File("/root/art/images/" + filename);
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs();
                }
                try {
                    file.transferTo(newFile);
                    return Result.newSuccess("/root/art/images/" + filename);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return Result.newError("上传失败");
    }

}
