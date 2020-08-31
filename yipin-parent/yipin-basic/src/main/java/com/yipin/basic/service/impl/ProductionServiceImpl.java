package com.yipin.basic.service.impl;

import VO.PageVO;
import VO.Result;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.yipin.basic.VO.UserVO;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.ProductionVO;
import com.yipin.basic.dao.productionDao.ProductionRepository;
import com.yipin.basic.dao.userDao.UserArtRepository;
import com.yipin.basic.dao.userDao.UserPerformanceRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.production.Production;
import com.yipin.basic.entity.user.User;
import com.yipin.basic.entity.user.UserArt;
import com.yipin.basic.entity.user.UserPerformance;
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

    /**分页查询代表作**/
    @Override
    public Result<PageVO<ProductionVO>> listPublishedProduction(PageArg arg) {
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<Production> productionPage = productionRepository
                .findProduction(pageable);

        //开始包装ProductionVO
        List<Production> productionList = productionPage.getContent();
        List<ProductionVO> productionVOList = new ArrayList<>();
        for (Production production : productionList) {
            ProductionVO productionVO = new ProductionVO();
            BeanUtils.copyProperties(production,productionVO);
            UserVO userVO = userService.getUserMsg(production.getUserId()).getData();
            if (userVO == null){
                return Result.newResult(ResultEnum.USER_NOT_EXIST);
            }
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

    /**上传作品信息,可选择保存或者直接发布**/
    @Override
    public Result<Void> uploadProduction(ProductionForm productionForm) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }
        User user = userRepository.findUserById(productionForm.getUserId());
        if (user == null){
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        if (user.getInformationStatus() == 0){
            return Result.newError("请先完成个人信息填写");
        }
        Production production = new Production();
        BeanUtils.copyProperties(productionForm,production);

        production.setCheckStatus(1); //先设置为过审核之后看看有没有审核需求
        production.setEvaluateStatus(0); //初始评估状态为0
        production.setIsMainProduction(0); //是否为代表作
        production.setCreateTime(new Date());
        production.setUpdateTime(new Date());
        production.setViews(0);
        production.setLikes(0);
        production.setComments(0);
        productionRepository.save(production);
        if (productionForm.getPublishStatus() == 1){
            UserPerformance userPerformance = userPerformanceRepository.findLastUserPerformance(user.getId());
            userPerformance.setUploadProductionNums(userPerformance.getUploadProductionNums() + 1);
            user.setProductionNum(user.getProductionNum() + 1);
            userPerformanceRepository.save(userPerformance);
            userRepository.save(user);
            //评估后才能作为代表作
            /*if (user.getMainProductionId() == null ){
                user.setMainProductionId(production.getId());
                production.setIsMainProduction(1);
                productionRepository.save(production);
            }*/
        }
        return Result.newSuccess();
    }

    /**将未公开的作品公开**/
    @Override
    public Result<Void> publishProduction(Integer id) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Production production = productionRepository.findProductionById(id);
        if (production == null){
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        if (production.getPublishStatus() == 1){
            return Result.newError("作品已公开，无需重复公开");
        }
        production.setPublishStatus(1);
        User user = userRepository.findUserById(production.getUserId());
        if (user.getMainProductionId() == null && production.getEvaluateStatus() == 1){
            user.setMainProductionId(production.getId());
            production.setIsMainProduction(1);
        }
        productionRepository.save(production);
        UserPerformance userPerformance = userPerformanceRepository.findLastUserPerformance(user.getId());
        userPerformance.setUploadProductionNums(userPerformance.getUploadProductionNums() + 1);
        user.setProductionNum(user.getProductionNum() + 1);
        userPerformanceRepository.save(userPerformance);
        userRepository.save(user);
        return Result.newSuccess();
    }

    /**将公开的作品设置为隐私**/
    @Override
    public Result<Void> privateProduction(Integer id) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Production production = productionRepository.findProductionById(id);
        if (production == null){
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        if (production.getPublishStatus() == 0){
            return Result.newError("作品已为私密，无需重复设置私密");
        }

        production.setPublishStatus(0);
        productionRepository.save(production);
        User user = userRepository.findUserById(production.getUserId());
        if (user.getMainProductionId() == production.getId()){
            List<Production> productionList = productionRepository.findProductionByPublishStatusAndUserIdAndEvaluateStatusOrderByCreateTimeDesc(1,user.getId(),1);
            if (productionList.isEmpty()){
                user.setMainProductionId(null);
            }else{
                user.setMainProductionId(productionList.get(0).getId());
                Production p = productionRepository.findProductionById(productionList.get(0).getId());
                p.setIsMainProduction(1);
                productionRepository.save(p);
            }
            production.setIsMainProduction(0);
            productionRepository.save(production);
        }
        UserPerformance userPerformance = userPerformanceRepository.findLastUserPerformance(user.getId());
        user.setProductionNum(user.getProductionNum() - 1);
        userPerformance.setUploadProductionNums(userPerformance.getUploadProductionNums() - 1);
        userPerformanceRepository.save(userPerformance);
        userRepository.save(user);
        return Result.newSuccess();
    }

    /**分页查询用户已公开的全部作品**/
    @Override
    public Result<PageVO<ProductionVO>> listUserPublishedProduction(PageArg arg, Integer userId) {
        if (userId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }

        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<Production> productionPage = productionRepository
                .findProductionByPublishStatusAndUserIdOrderByCreateTimeDesc(1,userId,pageable);

        //开始包装ProductionVO
        List<Production> productionList = productionPage.getContent();
        List<ProductionVO> productionVOList = new ArrayList<>();
        for (Production production : productionList) {
            ProductionVO productionVO = new ProductionVO();
            BeanUtils.copyProperties(production,productionVO);
            UserVO userVO = userService.getUserMsg(production.getUserId()).getData();
            if (userVO == null){
                return Result.newResult(ResultEnum.USER_NOT_EXIST);
            }
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

    /**分页查询用户还没公开的作品**/
    @Override
    public Result<PageVO<ProductionVO>> listUserUnPublishedProduction(PageArg arg, Integer userId) {
        if (userId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }

        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<Production> productionPage = productionRepository
                .findProductionByPublishStatusAndUserIdOrderByCreateTimeDesc(0,userId,pageable);

        //开始包装ProductionVO
        List<Production> productionList = productionPage.getContent();
        List<ProductionVO> productionVOList = new ArrayList<>();
        for (Production production : productionList) {
            ProductionVO productionVO = new ProductionVO();
            BeanUtils.copyProperties(production,productionVO);
            UserVO userVO = userService.getUserMsg(production.getUserId()).getData();
            if (userVO == null){
                return Result.newResult(ResultEnum.USER_NOT_EXIST);
            }
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

    /**为作品点赞**/
    @Override
    public Result<Void> likes(Integer id) {
        if (id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Production production = productionRepository.findProductionById(id);
        if (production == null){
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        User user = userRepository.findUserById(production.getUserId());
        if (user == null){
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        user.setLikes(user.getLikes() + 1);
        UserPerformance userPerformance = userPerformanceRepository.findLastUserPerformance(user.getId());
        userPerformance.setLikeNums(userPerformance.getLikeNums() + 1);
        production.setLikes(production.getLikes() + 1);
        userPerformanceRepository.save(userPerformance);
        productionRepository.save(production);
        userRepository.save(user);
        return Result.newSuccess();
    }

    /**浏览作品**/
    @Override
    public Result<Void> views(Integer id) {
        if (id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Production production = productionRepository.findProductionById(id);
        if (production == null){
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        production.setViews(production.getViews() + 1);
        return Result.newSuccess();
    }

    /**获取用户代表作**/
    @Override
    public Result<ProductionVO> getMainProduction(Integer userId) {
        if (userId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        User user = userRepository.findUserById(userId);
        if(user.getMainProductionId() == null){
            return Result.newError("您还未设置代表作");
        }
        Production production = productionRepository.findProductionById(user.getMainProductionId());
        if (user == null){
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        if (production == null){
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        ProductionVO productionVO = new ProductionVO();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        BeanUtils.copyProperties(production,productionVO);
        productionVO.setUserVO(userVO);
        return Result.newSuccess(productionVO);
    }

    /**根据标题搜索作品**/
    @Override
    public Result<PageVO<ProductionVO>> searchProduction(String title,PageArg arg) {
        if (title == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<Production> productionPage = productionRepository.findProductionByTitleLikes("%" + title + "%",pageable);
        List<Production> productionList = productionPage.getContent();

        List<ProductionVO> productionVOList = new ArrayList<>();
        for (Production production : productionList) {
            ProductionVO productionVO = new ProductionVO();
            BeanUtils.copyProperties(production,productionVO);
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

    /**根据id获取作品信息**/
    @Override
    public Result<ProductionVO> getProductionById(Integer id) {
        if (id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Production production = productionRepository.findProductionById(id);
        if (production == null){
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        User user = userRepository.findUserById(production.getUserId());
        if (user == null){
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        UserArt userArt = userArtRepository.findLastUserArt(production.getUserId());
        UserPerformance userPerformance = userPerformanceRepository.findLastUserPerformance(production.getUserId());
        UserVO userVO = new UserVO();
        ProductionVO productionVO = new ProductionVO();
        BeanUtils.copyProperties(production,productionVO);
        BeanUtils.copyProperties(user,userVO);
        userVO.setUserArt(userArt);
        userVO.setUserPerformance(userPerformance);
        productionVO.setUserVO(userVO);
        return Result.newSuccess(productionVO);
    }


}
