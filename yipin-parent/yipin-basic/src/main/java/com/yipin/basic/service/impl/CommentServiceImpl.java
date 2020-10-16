package com.yipin.basic.service.impl;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.CommentVO;
import com.yipin.basic.dao.othersDao.ArtActivityRepository;
import com.yipin.basic.dao.othersDao.ArtMsgRepository;
import com.yipin.basic.dao.othersDao.CommentRepository;
import com.yipin.basic.dao.othersDao.LikesRepository;
import com.yipin.basic.dao.productionDao.ProductionRepository;
import com.yipin.basic.dao.userDao.UserPerformanceRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.others.ArtActivity;
import com.yipin.basic.entity.others.ArtMsg;
import com.yipin.basic.entity.others.Comment;
import com.yipin.basic.entity.others.Likes;
import com.yipin.basic.entity.production.Production;
import com.yipin.basic.entity.user.User;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private ArtActivityRepository artActivityRepository;
    @Autowired
    private ArtMsgRepository artMsgRepository;

    /**评论作品，回复评论的话就传父评论的id**/
    @Override
    public Result<Void> commentProduction(CommentForm commentForm) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (commentForm.getProductionId() == null && commentForm.getActivityId() == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }

        Production production = productionRepository.findProductionById(commentForm.getProductionId());
        ArtActivity artActivity = artActivityRepository.findArtActivityById(commentForm.getActivityId());
        if (production == null && artActivity == null){
            return Result.newError("活动或者作品不存在");
        }
        if (production != null) {
            //生成消息
            ArtMsg artMsg = new ArtMsg();
            artMsg.setCreateTime(new Date());
            artMsg.setMsgDetail("在你的作品 "+ production.getTitle() +" 下评论："+ commentForm.getContent());
            artMsg.setUserId(commentForm.getUserId());
            artMsg.setReceiveUserId(production.getUserId());
            artMsg.setViewStatus(0);
            //用户品值参数增加，作品评论数增加
            production.setComments(production.getComments() + 1);
            UserPerformance userPerformance = userPerformanceRepository.findLastUserPerformance(production.getUserId());
            userPerformance.setCommentNums(userPerformance.getCommentNums() + 1);
            artMsgRepository.save(artMsg);
            userPerformanceRepository.save(userPerformance);
        }
        if (artActivity != null){
            artActivity.setComments(artActivity.getComments() + 1);
            artActivityRepository.save(artActivity);
        }
        if (commentForm.getParentCommentId() != null){
            Comment c = commentRepository.findCommentById(commentForm.getParentCommentId());
            //生成消息
            ArtMsg artMsg = new ArtMsg();
            artMsg.setCreateTime(new Date());
            artMsg.setMsgDetail("回复了你的评论："+ c.getContent() +" "+ commentForm.getContent());
            artMsg.setUserId(commentForm.getUserId());
            artMsg.setReceiveUserId(c.getUserId());
            artMsg.setViewStatus(0);
            artMsgRepository.save(artMsg);
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentForm,comment);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        comment.setDeleteStatus(false);
        comment.setLikes(0);
        commentRepository.save(comment);
        return Result.newSuccess();
    }

    /**分页获取作品评论信息**/
    @Override
    public Result<PageVO<CommentVO>> listComments(Integer productionId,PageArg arg) {
        if (productionId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<Comment> commentPage = commentRepository.findCommentByProductionIdOrderByLikesDesc(productionId,pageable);
        List<Comment> commentList = commentPage.getContent();
        List<CommentVO> commentVOList = new ArrayList<>();
        //包装评论视图层
        for (Comment comment : commentList) {
            CommentVO commentVO = new CommentVO();
            User user = userRepository.findUserById(comment.getUserId());
            BeanUtils.copyProperties(comment,commentVO);
            if (comment.getParentCommentId() != null){
                User parentUser = userRepository.findUserById(comment.getParentCommentId());
                if (parentUser != null){
                    commentVO.setParentCommentNickname(parentUser.getNickname());
                }
            }
            if (user != null) {
                commentVO.setAvatar(user.getAvatar());
                commentVO.setNickname(user.getNickname());
                commentVOList.add(commentVO);
            }
        }

        PageVO<CommentVO> pageVo = PageVO.<CommentVO>builder()
                .totalPage(commentPage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(commentVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**分页获取活动评论信息**/
    @Override
    public Result<PageVO<CommentVO>> listTopicArticleComments(Integer topicArticleId,PageArg arg) {
        if (topicArticleId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<Comment> commentPage = commentRepository.findCommentByActivityIdOrderByLikesDesc(topicArticleId,pageable);
        List<Comment> commentList = commentPage.getContent();
        List<CommentVO> commentVOList = new ArrayList<>();
        //包装评论视图层
        for (Comment comment : commentList) {
            CommentVO commentVO = new CommentVO();
            User user = userRepository.findUserById(comment.getUserId());
            BeanUtils.copyProperties(comment,commentVO);
            if (comment.getParentCommentId() != null){
                User parentUser = userRepository.findUserById(comment.getParentCommentId());
                if (parentUser != null){
                    commentVO.setParentCommentNickname(parentUser.getNickname());
                }
            }
            if (user != null) {
                commentVO.setAvatar(user.getAvatar());
                commentVO.setNickname(user.getNickname());
                commentVOList.add(commentVO);
            }
        }

        PageVO<CommentVO> pageVo = PageVO.<CommentVO>builder()
                .totalPage(commentPage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(commentVOList)
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
        if (comment == null || comment.getDeleteStatus()){
            return Result.newError("评论不存在或者已被删除");
        }
        Production production = productionRepository.findProductionById(comment.getProductionId());
        ArtActivity topicArticle = artActivityRepository.findArtActivityById(comment.getActivityId());
        if (production == null && topicArticle == null){
            return Result.newError("作品或者活动不存在");
        }
        if (production != null) {
            production.setComments(production.getComments() - 1);
            productionRepository.save(production);
        }
        if (topicArticle != null){
            topicArticle.setComments(topicArticle.getComments() - 1);
            artActivityRepository.save(topicArticle);
        }
        comment.setDeleteStatus(true);
        commentRepository.save(comment);
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
        if (comment == null || comment.getDeleteStatus()){
            return Result.newError("评论不存在或已被删除");
        }
        BeanUtils.copyProperties(commentForm,comment);
        commentRepository.save(comment);
        return Result.newSuccess();
    }

    /**为评论点赞**/
    @Override
    public Result<Void> likeComment(Integer userId,Integer commentId){
        if (userId == null || commentId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Likes likes = likesRepository.findLikesByUserIdAndCommentId(userId,commentId);
        if (likes != null){
            return Result.newError("已点赞，无需重复点赞");
        }
        Comment comment = commentRepository.findCommentById(commentId);
        if (comment == null || comment.getDeleteStatus()){
            return Result.newError("评论被删除或者不存在");
        }
        //生成消息
        ArtMsg artMsg = new ArtMsg();
        artMsg.setCreateTime(new Date());
        artMsg.setMsgDetail("为你的评论："+ comment.getContent() +" 点赞");
        artMsg.setUserId(userId);
        artMsg.setReceiveUserId(comment.getUserId());
        artMsg.setViewStatus(0);

        comment.setLikes(comment.getLikes() + 1);
        Likes l = new Likes();
        l.setCommentId(commentId);
        l.setUserId(userId);
        l.setCreateTime(new Date());
        artMsgRepository.save(artMsg);
        likesRepository.save(l);
        commentRepository.save(comment);
        return Result.newSuccess();
    }

    /**取消点赞**/
    @Override
    public Result<Void> unlikeComment(Integer userId,Integer commentId){
        if (userId == null || commentId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Likes likes = likesRepository.findLikesByUserIdAndCommentId(userId,commentId);
        if (likes == null){
            return Result.newError("您还未点赞");
        }
        Comment comment = commentRepository.findCommentById(commentId);
        if (comment == null || comment.getDeleteStatus()){
            return Result.newError("评论被删除或者不存在");
        }
        comment.setLikes(comment.getLikes() - 1);
        likesRepository.delete(likes);
        commentRepository.save(comment);
        return Result.newSuccess();
    }
}
