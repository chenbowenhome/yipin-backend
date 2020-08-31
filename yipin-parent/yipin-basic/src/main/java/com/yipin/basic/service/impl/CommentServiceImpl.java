package com.yipin.basic.service.impl;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.dao.CommentRepository;
import com.yipin.basic.dao.productionDao.ProductionRepository;
import com.yipin.basic.dao.userDao.UserPerformanceRepository;
import com.yipin.basic.entity.Comment;
import com.yipin.basic.entity.production.Production;
import com.yipin.basic.entity.user.UserPerformance;
import com.yipin.basic.form.CommentForm;
import com.yipin.basic.service.CommentService;
import enums.ResultEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProductionRepository productionRepository;
    @Autowired
    private UserPerformanceRepository userPerformanceRepository;
    @Autowired
    private HttpServletRequest request;

    /**评论作品，回复评论的话就传父评论的id**/
    @Override
    public Result<Void> commentProduction(CommentForm commentForm) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (commentForm.getParentCommentId() != null){
            Comment comment = commentRepository.findCommentById(commentForm.getParentCommentId());
            if (comment == null){
                return Result.newError("评论已被删除，无法回复该评论");
            }
        }
        Production production = productionRepository.findProductionById(commentForm.getProductionId());
        if (production == null){
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        production.setComments(production.getComments() + 1);
        UserPerformance userPerformance = userPerformanceRepository.findLastUserPerformance(production.getUserId());
        userPerformance.setCommentNums(userPerformance.getCommentNums() + 1);
        userPerformanceRepository.save(userPerformance);
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentForm,comment);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        commentRepository.save(comment);
        return Result.newSuccess();
    }

    /**分页获取作品评论信息**/
    @Override
    public Result<PageVO<Comment>> listComments(Integer productionId,PageArg arg) {
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<Comment> commentPage = commentRepository.findCommentByProductionIdOrderByCreateTimeDesc(productionId,pageable);
        PageVO<Comment> pageVo = PageVO.<Comment>builder()
                .totalPage(commentPage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(commentPage.getContent())
                .build();
        return Result.newSuccess(pageVo);
    }

    /**删除一条评论**/
    @Override
    public Result<Void> deleteComment(Integer id) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Comment comment = commentRepository.findCommentById(id);
        if (comment == null){
            return Result.newError("评论不存在");
        }
        Production production = productionRepository.findProductionById(comment.getProductionId());
        if (production == null){
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        production.setComments(production.getComments() - 1);
        UserPerformance userPerformance = userPerformanceRepository.findLastUserPerformance(production.getUserId());
        userPerformance.setCommentNums(userPerformance.getCommentNums() - 1);
        userPerformanceRepository.save(userPerformance);
        productionRepository.save(production);
        commentRepository.deleteById(id);
        return Result.newSuccess();
    }

    /**更新一条评论**/
    @Override
    public Result<Void> updateComment(Integer id, CommentForm commentForm) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Comment comment = commentRepository.findCommentById(id);
        if (comment == null){
            return Result.newError("评论不存在");
        }
        BeanUtils.copyProperties(commentForm,comment);
        commentRepository.save(comment);
        return Result.newSuccess();
    }
}
