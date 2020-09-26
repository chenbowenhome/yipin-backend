package com.yipin.basic.service.impl;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.CommentVO;
import com.yipin.basic.dao.othersDao.ArtMsgRepository;
import com.yipin.basic.dao.othersDao.CommentRepository;
import com.yipin.basic.dao.othersDao.LikesRepository;
import com.yipin.basic.dao.othersDao.TopicArticleRepository;
import com.yipin.basic.dao.productionDao.ProductionRepository;
import com.yipin.basic.dao.userDao.UserPerformanceRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.others.ArtMsg;
import com.yipin.basic.entity.others.Comment;
import com.yipin.basic.entity.others.Likes;
import com.yipin.basic.entity.others.TopicArticle;
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
import org.springframework.security.core.parameters.P;
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
    private TopicArticleRepository topicArticleRepository;
    @Autowired
    private ArtMsgRepository artMsgRepository;

    /**评论作品，回复评论的话就传父评论的id**/
    @Override
    public Result<Void> commentProduction(CommentForm commentForm) {
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }
        if (commentForm.getProductionId() == null && commentForm.getTopicArticleId() == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        if (commentForm.getParentCommentId() != null){
            Comment comment = commentRepository.findCommentById(commentForm.getParentCommentId());
            if (comment == null || comment.getDeleteStatus()){
                return Result.newError("评论已被删除，无法回复该评论");
            }
        }
        Production production = productionRepository.findProductionById(commentForm.getProductionId());
        TopicArticle topicArticle = topicArticleRepository.findTopicArticleById(commentForm.getTopicArticleId());
        if (production == null && topicArticle == null){
            return Result.newError("话题或者作品不存在");
        }
        if (production != null) {
            //生成消息
            ArtMsg artMsg = new ArtMsg();
            artMsg.setCreateTime(new Date());
            artMsg.setMsgDetail("在你的作品 "+ production.getTitle() +" 下评论："+ commentForm.getContent());
            artMsg.setUserId(production.getUserId());
            artMsg.setViewStatus(0);
            //用户品值参数增加
            production.setComments(production.getComments() + 1);
            UserPerformance userPerformance = userPerformanceRepository.findLastUserPerformance(production.getUserId());
            userPerformance.setCommentNums(userPerformance.getCommentNums() + 1);
            artMsgRepository.save(artMsg);
            userPerformanceRepository.save(userPerformance);
        }
        if (topicArticle != null){
            topicArticle.setComments(topicArticle.getComments() + 1);
            topicArticleRepository.save(topicArticle);
        }
        if (commentForm.getMainCommentId() != null && commentForm.getParentCommentId() == null){
            Comment c = commentRepository.findCommentById(commentForm.getMainCommentId());
            //生成消息
            ArtMsg artMsg = new ArtMsg();
            artMsg.setCreateTime(new Date());
            artMsg.setMsgDetail("回复了你的评论："+ c.getContent() +" "+ commentForm.getContent());
            artMsg.setUserId(c.getUserId());
            artMsg.setViewStatus(0);
            artMsgRepository.save(artMsg);
        }
        if (commentForm.getParentCommentId() != null){
            Comment c = commentRepository.findCommentById(commentForm.getParentCommentId());
            //生成消息
            ArtMsg artMsg = new ArtMsg();
            artMsg.setCreateTime(new Date());
            artMsg.setMsgDetail("回复了你的评论："+ c.getContent() +" "+ commentForm.getContent());
            artMsg.setUserId(c.getUserId());
            artMsg.setViewStatus(0);
            artMsgRepository.save(artMsg);
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentForm,comment);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        comment.setDeleteStatus(false);
        comment.setLikes(0);
        comment.setKidCommentNums(0);
        Comment c = commentRepository.findCommentById(commentForm.getMainCommentId());
        if (c != null){
            c.setKidCommentNums(c.getKidCommentNums() + 1);
            commentRepository.save(c);
        }
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
        Page<Comment> commentPage = commentRepository.findCommentByProductionIdAndMainCommentIdOrderByLikesDesc(productionId,null,pageable);
        List<Comment> commentList = commentPage.getContent();
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentVO commentVO = new CommentVO();
            List<Comment> replyCommentList = commentRepository.findCommentByMainCommentIdOrderByLikesDesc(comment.getId());
            User user = userRepository.findUserById(comment.getUserId());
            BeanUtils.copyProperties(comment,commentVO);
            commentVO.setAvatar(user.getAvatar());
            commentVO.setNickname(user.getNickname());
            List<CommentVO> replyCommentsVO = new ArrayList<>();
            for (Comment c : replyCommentList) {
                CommentVO replyCommentVO = new CommentVO();
                BeanUtils.copyProperties(c,replyCommentVO);
                User user1 = userRepository.findUserById(c.getUserId());
                if (c.getParentCommentId() != null){
                    Comment parentComment = commentRepository.findCommentById(c.getParentCommentId());
                    if (parentComment != null) {
                        User parentUser = userRepository.findUserById(parentComment.getUserId());
                        replyCommentVO.setParentCommentNickname(parentUser.getNickname());
                    }
                }
                replyCommentVO.setNickname(user1.getNickname());
                replyCommentVO.setAvatar(user1.getAvatar());
                replyCommentsVO.add(replyCommentVO);
            }
            commentVO.setReplyCommentList(replyCommentsVO);
            commentVOList.add(commentVO);
        }

        PageVO<CommentVO> pageVo = PageVO.<CommentVO>builder()
                .totalPage(commentPage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(commentVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**分页获取话题文章评论信息**/
    @Override
    public Result<PageVO<CommentVO>> listTopicArticleComments(Integer topicArticleId,PageArg arg) {
        if (topicArticleId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<Comment> commentPage = commentRepository.findCommentByTopicArticleIdAndMainCommentIdOrderByLikesDesc(topicArticleId,null,pageable);
        List<Comment> commentList = commentPage.getContent();
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentVO commentVO = new CommentVO();
            List<Comment> replyCommentList = commentRepository.findCommentByMainCommentIdOrderByLikesDesc(comment.getId());
            User user = userRepository.findUserById(comment.getUserId());
            BeanUtils.copyProperties(comment,commentVO);
            commentVO.setAvatar(user.getAvatar());
            commentVO.setNickname(user.getNickname());
            List<CommentVO> replyCommentsVO = new ArrayList<>();
            for (Comment c : replyCommentList) {
                CommentVO replyCommentVO = new CommentVO();
                BeanUtils.copyProperties(c,replyCommentVO);
                User user1 = userRepository.findUserById(c.getUserId());
                if (c.getParentCommentId() != null){
                    Comment parentComment = commentRepository.findCommentById(c.getParentCommentId());
                    if (parentComment != null) {
                        User parentUser = userRepository.findUserById(parentComment.getUserId());
                        replyCommentVO.setParentCommentNickname(parentUser.getNickname());
                    }
                }
                replyCommentVO.setNickname(user1.getNickname());
                replyCommentVO.setAvatar(user1.getAvatar());
                replyCommentsVO.add(replyCommentVO);
            }
            commentVO.setReplyCommentList(replyCommentsVO);
            commentVOList.add(commentVO);
        }

        PageVO<CommentVO> pageVo = PageVO.<CommentVO>builder()
                .totalPage(commentPage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(commentVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**分页获取评论子评论信息**/
    @Override
    public Result<PageVO<CommentVO>> listReplyComments(Integer mainCommentId, PageArg arg) {
        if (mainCommentId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<Comment> replyCommentPage = commentRepository.findCommentByMainCommentIdOrderByLikesDesc(mainCommentId,pageable);
        List<Comment> commentList = replyCommentPage.getContent();
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentVO commentVO = new CommentVO();
            User user = userRepository.findUserById(comment.getUserId());
            BeanUtils.copyProperties(comment,commentVO);
            if (comment.getParentCommentId() != null){
                Comment parentComment = commentRepository.findCommentById(comment.getParentCommentId());
                if (parentComment != null) {
                    User parentUser = userRepository.findUserById(parentComment.getUserId());
                    commentVO.setParentCommentNickname(parentUser.getNickname());
                }
            }
            commentVO.setNickname(user.getNickname());
            commentVO.setAvatar(user.getAvatar());
            commentVOList.add(commentVO);
        }
        PageVO<CommentVO> pageVo = PageVO.<CommentVO>builder()
                .totalPage(replyCommentPage.getTotalPages())
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
        TopicArticle topicArticle = topicArticleRepository.findTopicArticleById(comment.getTopicArticleId());
        if (production == null && topicArticle == null){
            return Result.newError("作品或者话题不存在");
        }
        if (production != null) {
            production.setComments(production.getComments() - 1);
            productionRepository.save(production);
        }
        if (topicArticle != null){
            topicArticle.setComments(topicArticle.getComments() - 1);
            topicArticleRepository.save(topicArticle);
        }
        commentRepository.delete(comment);
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
