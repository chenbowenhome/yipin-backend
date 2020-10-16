package com.yipin.basic.service.impl;

import VO.PageVO;
import VO.Result;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.yipin.basic.VO.UserVO;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.ProductionVO;
import com.yipin.basic.dao.othersDao.ArtMsgRepository;
import com.yipin.basic.dao.othersDao.LikesRepository;
import com.yipin.basic.dao.productionDao.ProductionRepository;
import com.yipin.basic.dao.productionDao.ProductionTagRepository;
import com.yipin.basic.dao.userDao.UserArtRepository;
import com.yipin.basic.dao.userDao.UserPerformanceRepository;
import com.yipin.basic.dao.userDao.UserProductionCollectionsRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.others.ArtMsg;
import com.yipin.basic.entity.others.Likes;
import com.yipin.basic.entity.production.Production;
import com.yipin.basic.entity.production.ProductionTag;
import com.yipin.basic.entity.user.User;
import com.yipin.basic.entity.user.UserArt;
import com.yipin.basic.entity.user.UserPerformance;
import com.yipin.basic.entity.user.UserProductionCollections;
import com.yipin.basic.form.ProductionForm;
import com.yipin.basic.service.ProductionService;
import com.yipin.basic.service.UserService;
import enums.ResultEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class ProductionServiceImpl implements ProductionService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductionRepository productionRepository;
    @Autowired
    private UserArtRepository userArtRepository;
    @Autowired
    private UserPerformanceRepository userPerformanceRepository;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private ProductionTagRepository productionTagRepository;
    @Autowired
    private ArtMsgRepository artMsgRepository;
    @Autowired
    private UserProductionCollectionsRepository userProductionCollectionsRepository;

    /**
     * 分页查询代表作
     **/
    @Override
    public Result<PageVO<ProductionVO>> listPublishedProduction(PageArg arg) {
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1, arg.getPageSize());
        Page<Production> productionPage = productionRepository
                .findProductions(pageable);

        //开始包装ProductionVO
        List<Production> productionList = productionPage.getContent();
        List<ProductionVO> productionVOList = new ArrayList<>();
        for (Production production : productionList) {
            ProductionVO productionVO = new ProductionVO();
            BeanUtils.copyProperties(production, productionVO);
            UserVO userVO = userService.getUserMsg(production.getUserId()).getData();
            if (userVO == null) {
                continue;
            }
            //获取分类标签名称
            ProductionTag productionTag = productionTagRepository.findProductionTagById(production.getUserId());
            String tagName = null;
            if (productionTag == null) {
                tagName = "该标签已被删除";
            } else {
                tagName = productionTag.getTagName();
            }
            //获取用户艺能
            UserArt userArt = userArtRepository.findLastUserArt(userVO.getId());
            userVO.setUserArt(userArt);
            productionVO.setTagName(tagName);
            productionVO.setUserVO(userVO);
            productionVOList.add(productionVO);
        }
        int total = productionPage.getTotalPages();

        //构建pageVo对象
        PageVO<ProductionVO> pageVo = PageVO.<ProductionVO>builder()
                .totalPage(total)
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(productionVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**
     * 上传作品信息,可选择保存或者直接发布
     **/
    @Override
    public Result<Void> uploadProduction(ProductionForm productionForm) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)) {
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }
        User user = userRepository.findUserById(productionForm.getUserId());
        if (user == null) {
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        if (user.getInformationStatus() == 0) {
            return Result.newError("请先完成个人信息填写");
        }
        Production production = new Production();
        BeanUtils.copyProperties(productionForm, production);

        production.setCheckStatus(1); //先设置为过审核之后看看有没有审核需求
        production.setCreateTime(new Date());
        production.setUpdateTime(new Date());
        production.setViews(0);
        production.setLikes(0);
        production.setComments(0);
        production.setDeleteStatus(0); //默认为0未删除
        productionRepository.save(production);

        UserPerformance userPerformance = userPerformanceRepository.findLastUserPerformance(user.getId());
        userPerformance.setUploadProductionNums(userPerformance.getUploadProductionNums() + 1);
        List<Integer> ids = user.getMainProductionId();
        if (production.getPublishStatus() == 1) {
            user.setProductionNum(user.getProductionNum() + 1);
        }
        if (production.getEvaluateStatus() == 1) {
            user.setAssessments(user.getAssessments() + 1);
        }
        if (ids.size() >= 5) {
            production.setIsMainProduction(0);
        } else {
            if (production.getIsMainProduction() == 1) {
                ids.add(production.getId());
                user.setMainProductionId(ids);
            }
        }
        userRepository.save(user);
        userPerformanceRepository.save(userPerformance);
        return Result.newSuccess();
    }

    /**
     * 将未公开的作品公开
     **/
    @Override
    public Result<Void> publishProduction(Integer id) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)) {
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (id == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Production production = productionRepository.findProductionById(id);
        if (production == null) {
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        if (production.getPublishStatus() == 1) {
            return Result.newError("作品已公开，无需重复公开");
        }
        production.setPublishStatus(1);
        User user = userRepository.findUserById(production.getUserId());
        user.setProductionNum(user.getProductionNum() + 1);
        productionRepository.save(production);
        userRepository.save(user);
        return Result.newSuccess();
    }

    /**
     * 将公开的作品设置为隐私
     **/
    @Override
    public Result<Void> privateProduction(Integer id) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)) {
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (id == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Production production = productionRepository.findProductionById(id);
        if (production == null) {
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        if (production.getPublishStatus() == 0) {
            return Result.newError("作品已为私密，无需重复设置私密");
        }

        production.setPublishStatus(0);
        User user = userRepository.findUserById(production.getUserId());
        user.setProductionNum(user.getProductionNum() - 1);
        //如果为代表作，则取消代表作状态
        if (production.getIsMainProduction() == 1) {
            production.setIsMainProduction(0);
            List<Integer> ids = user.getMainProductionId();
            ids.remove(production.getId());
            user.setMainProductionId(ids);
        }
        userRepository.save(user);
        productionRepository.save(production);
        return Result.newSuccess();
    }

    /**
     * 分页查询用户已公开的全部作品
     **/
    @Override
    public Result<PageVO<ProductionVO>> listUserPublishedProduction(PageArg arg, Integer userId) {
        if (userId == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }

        Pageable pageable = PageRequest.of(arg.getPageNo() - 1, arg.getPageSize());
        Page<Production> productionPage = productionRepository
                .findProductionByPublishStatusAndUserIdAndDeleteStatusOrderByCreateTimeDesc(1, userId, 0, pageable); //查询用户未删除且已发布的作品

        //开始包装ProductionVO
        List<Production> productionList = productionPage.getContent();
        List<ProductionVO> productionVOList = new ArrayList<>();
        for (Production production : productionList) {
            ProductionVO productionVO = new ProductionVO();
            BeanUtils.copyProperties(production, productionVO);
            //判断是否被用户收藏
            UserProductionCollections u = userProductionCollectionsRepository.findUserProductionCollectionsByUserIdAndProductionId(userId,production.getId());
            if (u != null){
                productionVO.setIsCollected(1);
            }else{
                productionVO.setIsCollected(0);
            }
            UserVO userVO = userService.getUserMsg(production.getUserId()).getData();
            if (userVO == null) {
                return Result.newResult(ResultEnum.USER_NOT_EXIST);
            }
            //获取分类标签名称
            ProductionTag productionTag = productionTagRepository.findProductionTagById(production.getTagId());
            String tagName = null;
            if (productionTag == null) {
                tagName = "该标签已被删除";
            } else {
                tagName = productionTag.getTagName();
            }
            productionVO.setTagName(tagName);
            productionVO.setUserVO(userVO);
            productionVOList.add(productionVO);
        }
        int total = productionPage.getTotalPages();

        //构建pageVo对象
        PageVO<ProductionVO> pageVo = PageVO.<ProductionVO>builder()
                .totalPage(total)
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(productionVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**
     * 分页查询用户还没公开的作品
     **/
    @Override
    public Result<PageVO<ProductionVO>> listUserUnPublishedProduction(PageArg arg, Integer userId) {
        if (userId == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }

        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)) {
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        Pageable pageable = PageRequest.of(arg.getPageNo() - 1, arg.getPageSize());
        Page<Production> productionPage = productionRepository
                .findProductionByPublishStatusAndUserIdAndDeleteStatusOrderByCreateTimeDesc(0, userId, 0, pageable);

        //开始包装ProductionVO
        List<Production> productionList = productionPage.getContent();
        List<ProductionVO> productionVOList = new ArrayList<>();
        for (Production production : productionList) {
            ProductionVO productionVO = new ProductionVO();
            BeanUtils.copyProperties(production, productionVO);
            UserProductionCollections u = userProductionCollectionsRepository.findUserProductionCollectionsByUserIdAndProductionId(userId,production.getId());
            if (u != null){
                productionVO.setIsCollected(1);
            }else{
                productionVO.setIsCollected(0);
            }
            UserVO userVO = userService.getUserMsg(production.getUserId()).getData();
            if (userVO == null) {
                return Result.newResult(ResultEnum.USER_NOT_EXIST);
            }
            //获取分类标签名称
            ProductionTag productionTag = productionTagRepository.findProductionTagById(production.getTagId());
            String tagName = null;
            if (productionTag == null) {
                tagName = "该标签已被删除";
            } else {
                tagName = productionTag.getTagName();
            }
            productionVO.setTagName(tagName);
            productionVO.setUserVO(userVO);
            productionVOList.add(productionVO);
        }
        int total = productionPage.getTotalPages();

        //构建pageVo对象
        PageVO<ProductionVO> pageVo = PageVO.<ProductionVO>builder()
                .totalPage(total)
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(productionVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**
     * 为作品点赞
     **/
    @Override
    public Result<Void> likes(Integer id, Integer userId) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)) {
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }
        if (id == null || userId == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Production production = productionRepository.findProductionById(id);
        if (production == null || production.getDeleteStatus() == 1) {
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        Likes l = likesRepository.findLikesByUserIdAndProductionId(userId, id);
        if (l != null) {
            return Result.newError("已点赞，无需重复点赞");
        }
        //被点赞用户
        User user = userRepository.findUserById(production.getUserId());
        //点赞用户
        UserPerformance userPerformance = userPerformanceRepository.findLastUserPerformance(userId);
        if (user == null || userPerformance == null) {
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        user.setLikes(user.getLikes() + 1);
        userPerformance.setLikeNums(userPerformance.getLikeNums() + 1);
        production.setLikes(production.getLikes() + 1);
        //生成消息
        ArtMsg artMsg = new ArtMsg();
        artMsg.setCreateTime(new Date());
        artMsg.setMsgDetail("为你的作品：" + production.getTitle() + " 点赞");
        artMsg.setUserId(userId);
        artMsg.setReceiveUserId(production.getUserId());
        artMsg.setViewStatus(0);

        //生成点赞信息
        Likes likes = new Likes();
        likes.setProductionId(production.getId());
        likes.setUserId(userId);
        likes.setCreateTime(new Date());
        artMsgRepository.save(artMsg);
        likesRepository.save(likes);
        userPerformanceRepository.save(userPerformance);
        productionRepository.save(production);
        userRepository.save(user);
        return Result.newSuccess();
    }

    /**
     * 为取消点赞
     **/
    @Override
    public Result<Void> unlikes(Integer id, Integer userId) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)) {
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }
        if (id == null || userId == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Production production = productionRepository.findProductionById(id);
        if (production == null || production.getDeleteStatus() == 1) {
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        Likes likes = likesRepository.findLikesByUserIdAndProductionId(userId, id);
        if (likes == null) {
            return Result.newError("您还未点赞");
        }
        //被点赞用户
        User user = userRepository.findUserById(production.getUserId());
        //点赞用户
        UserPerformance userPerformance = userPerformanceRepository.findLastUserPerformance(userId);
        if (user == null || userPerformance == null) {
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        user.setLikes(user.getLikes() - 1);
        userPerformance.setLikeNums(userPerformance.getLikeNums() - 1);
        production.setLikes(production.getLikes() - 1);
        //删除点赞信息
        likesRepository.delete(likes);
        userPerformanceRepository.save(userPerformance);
        productionRepository.save(production);
        userRepository.save(user);
        return Result.newSuccess();
    }

    /**
     * 浏览作品
     **/
    @Override
    public Result<Void> views(Integer id) {
        if (id == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Production production = productionRepository.findProductionById(id);
        if (production == null || production.getDeleteStatus() == 1) {
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        production.setViews(production.getViews() + 1);
        return Result.newSuccess();
    }

    /**
     * 获取用户代表作
     **/
    @Override
    public Result<List<ProductionVO>> getMainProduction(Integer userId) {
        if (userId == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        User user = userRepository.findUserById(userId);
        if (user == null) {
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        if (user.getMainProductionId().isEmpty()) {
            return Result.newError("您还未设置代表作");
        }
        List<Production> productions = productionRepository.findProductionByIsMainProductionAndUserIdOrderByCreateTimeDesc(1, userId);
        List<ProductionVO> productionVOList = new ArrayList<>();
        for (Production production : productions) {
            ProductionVO productionVO = new ProductionVO();
            BeanUtils.copyProperties(production, productionVO);
            //获取分类标签名称
            ProductionTag productionTag = productionTagRepository.findProductionTagById(production.getTagId());
            String tagName = null;
            if (productionTag == null) {
                tagName = "该标签已被删除";
            } else {
                tagName = productionTag.getTagName();
            }
            productionVO.setTagName(tagName);
            productionVOList.add(productionVO);
        }
        return Result.newSuccess(productionVOList);
    }

    /**
     * 根据标题搜索作品
     **/
    @Override
    public Result<PageVO<ProductionVO>> searchProduction(String title, PageArg arg) {
        if (title == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1, arg.getPageSize());
        Page<Production> productionPage = productionRepository.findProductionByTitleLikes("%" + title + "%", pageable);
        List<Production> productionList = productionPage.getContent();

        List<ProductionVO> productionVOList = new ArrayList<>();
        for (Production production : productionList) {
            ProductionVO productionVO = new ProductionVO();
            BeanUtils.copyProperties(production, productionVO);
            //获取分类标签名称
            ProductionTag productionTag = productionTagRepository.findProductionTagById(production.getTagId());
            String tagName = null;
            if (productionTag == null) {
                tagName = "该标签已被删除";
            } else {
                tagName = productionTag.getTagName();
            }
            productionVO.setTagName(tagName);
            productionVOList.add(productionVO);
        }
        //构建pageVo对象
        PageVO<ProductionVO> pageVo = PageVO.<ProductionVO>builder()
                .totalPage(productionPage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(productionVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**
     * 根据id获取作品信息
     **/
    @Override
    public Result<ProductionVO> getProductionById(Integer id,Integer userId) {
        if (id == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Production production = productionRepository.findProductionById(id);
        if (production == null || production.getDeleteStatus() == 1) {
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        User user = userRepository.findUserById(production.getUserId());
        if (user == null) {
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        UserArt userArt = userArtRepository.findLastUserArt(production.getUserId());
        UserPerformance userPerformance = userPerformanceRepository.findLastUserPerformance(production.getUserId());
        UserVO userVO = new UserVO();
        ProductionVO productionVO = new ProductionVO();
        BeanUtils.copyProperties(production, productionVO);
        //判断用户是否收藏
        if (userId != null) {
            UserProductionCollections u = userProductionCollectionsRepository.findUserProductionCollectionsByUserIdAndProductionId(userId, production.getId());
            if (u != null) {
                productionVO.setIsCollected(1);
            } else {
                productionVO.setIsCollected(0);
            }
        }
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserArt(userArt);
        userVO.setUserPerformance(userPerformance);
        //获取分类标签名称
        ProductionTag productionTag = productionTagRepository.findProductionTagById(production.getTagId());
        String tagName = null;
        if (productionTag == null) {
            tagName = "该标签已被删除";
        } else {
            tagName = productionTag.getTagName();
        }
        productionVO.setTagName(tagName);
        productionVO.setUserVO(userVO);
        return Result.newSuccess(productionVO);
    }

    /**
     * 获取所有分类标签
     **/
    @Override
    public Result<List<ProductionTag>> listProductionTags() {
        List<ProductionTag> productionTagList = productionTagRepository.findProductionTagByOrderByOrderNumAsc();
        return Result.newSuccess(productionTagList);
    }

    /**
     * 根据作品标题或者描述查询作品
     **/
    @Override
    public Result<PageVO<ProductionVO>> findProductionByKey(String key, PageArg arg) {
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1, arg.getPageSize());
        Page<Production> productionPage = productionRepository.findProductionByKeyLikes("%" + key + "%", pageable);

        //开始包装ProductionVO
        List<Production> productionList = productionPage.getContent();
        List<ProductionVO> productionVOList = new ArrayList<>();
        for (Production production : productionList) {
            ProductionVO productionVO = new ProductionVO();
            BeanUtils.copyProperties(production, productionVO);
            UserVO userVO = userService.getUserMsg(production.getUserId()).getData();
            if (userVO == null) {
                return Result.newResult(ResultEnum.USER_NOT_EXIST);
            }
            //获取分类标签名称
            ProductionTag productionTag = productionTagRepository.findProductionTagById(production.getTagId());
            String tagName = null;
            if (productionTag == null) {
                tagName = "该标签已被删除";
            } else {
                tagName = productionTag.getTagName();
            }
            productionVO.setTagName(tagName);
            productionVO.setUserVO(userVO);
            productionVOList.add(productionVO);
        }
        int total = productionPage.getTotalPages();

        //构建pageVo对象
        PageVO<ProductionVO> pageVo = PageVO.<ProductionVO>builder()
                .totalPage(total)
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(productionVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**
     * 删除用户自己的作品
     **/
    @Override
    public Result<Void> deleteProductionById(Integer userId, Integer productionId) {
        if (userId == null || productionId == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Production production = productionRepository.findProductionById(productionId);
        if (production == null || production.getDeleteStatus() == 1) {
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        if (production.getUserId() != userId) {
            return Result.newError("只能删除自己的作品");
        }
        User user = userRepository.findUserById(userId);
        production.setDeleteStatus(1);
        if (production.getPublishStatus() == 1) {
            user.setProductionNum(user.getProductionNum() - 1);
        }
        if (production.getIsMainProduction() == 1) {
            production.setIsMainProduction(0);
            List<Integer> ids = user.getMainProductionId();
            ids.remove(production.getId());
            user.setMainProductionId(ids);
        }
        userRepository.save(user);
        productionRepository.save(production);
        return Result.newSuccess();
    }

    /**
     * 取消代表作
     **/
    @Override
    public Result<Void> cancelMainProduction(Integer userId, Integer productionId) {
        if (userId == null || productionId == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Production production = productionRepository.findProductionById(productionId);
        if (production == null || production.getDeleteStatus() == 1) {
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        if (production.getIsMainProduction() == 0) {
            return Result.newError("该作品不是代表作");
        }
        if (production.getUserId() != userId) {
            return Result.newError("只能取消自己的作品");
        }
        User user = userRepository.findUserById(userId);
        production.setIsMainProduction(0);
        List<Integer> ids = user.getMainProductionId();
        ids.remove(production.getId());
        user.setMainProductionId(ids);
        userRepository.save(user);
        productionRepository.save(production);
        return Result.newSuccess();
    }


}
