package com.yipin.basic.service.impl;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.ArtActivityVO;
import com.yipin.basic.dao.othersDao.ArtActivityParticipantsRepository;
import com.yipin.basic.dao.othersDao.ArtActivityRepository;
import com.yipin.basic.dao.othersDao.DailySentenceRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.others.ArtActivity;
import com.yipin.basic.entity.others.ArtActivityParticipants;
import com.yipin.basic.entity.others.DailySentence;
import com.yipin.basic.entity.user.User;
import com.yipin.basic.form.ArtActivityForm;
import com.yipin.basic.service.ArtActivityService;
import enums.ResultEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ArtActivityServiceImpl implements ArtActivityService {

    @Autowired
    private ArtActivityRepository artActivityRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArtActivityParticipantsRepository artActivityParticipantsRepository;
    @Autowired
    private DailySentenceRepository dailySentenceRepository;

    /**
     * 通过id获取活动详情
     **/
    @Override
    public Result<ArtActivityVO> getArticleById(Integer id,Integer userId) {
        if (id == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        ArtActivity topicArticle = artActivityRepository.findArtActivityById(id);
        if (topicArticle == null) {
            return Result.newError("活动不存在");
        }
        ArtActivityParticipants artActivityParticipants = artActivityParticipantsRepository.findArtActivityParticipantsByUserIdAndArtActivityId(userId, topicArticle.getId());
        topicArticle.setViews(topicArticle.getViews() + 1);
        artActivityRepository.save(topicArticle);
        ArtActivityVO artActivityVO = new ArtActivityVO();
        BeanUtils.copyProperties(topicArticle, artActivityVO);
        if (artActivityParticipants != null) {
            artActivityVO.setIsJoin(1);
        }else {
            artActivityVO.setIsJoin(0);
        }
        return Result.newSuccess(artActivityVO);
    }

    /**
     * 上传活动
     **/
    @Override
    public Result<Void> addTopicArticle(ArtActivityForm artActivityForm) {
        ArtActivity artActivity = new ArtActivity();
        BeanUtils.copyProperties(artActivityForm, artActivity);
        artActivity.setCreateTime(new Date());
        artActivity.setComments(0);
        artActivity.setLikes(0);
        artActivity.setViews(0);
        artActivity.setParticipants(0);
        artActivity.setIsEnd(0);
        artActivityRepository.save(artActivity);
        return Result.newSuccess();
    }

    /**
     * 分页获取最新话题
     **/
    @Override
    public Result<PageVO<ArtActivityVO>> listTopicArticle(Integer userId, PageArg arg) {
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1, arg.getPageSize());
        //查找非轮播图的活动
        Page<ArtActivity> artActivityPage = artActivityRepository.findArtActivityBySlideStatusOrderByCreateTimeDesc(0,pageable);
        List<ArtActivity> artActivityList = artActivityPage.getContent();
        List<ArtActivityVO> artActivityVOList = new ArrayList<>();
        for (ArtActivity artActivity : artActivityList) {
            ArtActivityVO artActivityVO = new ArtActivityVO();
            ArtActivityParticipants artActivityParticipants = artActivityParticipantsRepository.findArtActivityParticipantsByUserIdAndArtActivityId(userId, artActivity.getId());
            BeanUtils.copyProperties(artActivity, artActivityVO);
            artActivityVO.setContent(null);
            if (artActivityParticipants != null) {
                artActivityVO.setIsJoin(1);
            } else {
                artActivityVO.setIsJoin(0);
            }
            artActivityVOList.add(artActivityVO);
        }
        //构建pageVo对象
        PageVO<ArtActivityVO> pageVo = PageVO.<ArtActivityVO>builder()
                .totalPage(artActivityPage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(artActivityVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**
     * 获取活动轮播图
     **/
    @Override
    public Result<List<ArtActivityVO>> listTopicArticleSlide(Integer userId) {
        //查找轮播图的活动
        List<ArtActivity> artActivityList = artActivityRepository.findArtActivityBySlideStatusOrderByCreateTimeDesc(1);
        List<ArtActivityVO> artActivityVOList = new ArrayList<>();
        for (ArtActivity artActivity : artActivityList) {
            ArtActivityVO artActivityVO = new ArtActivityVO();
            ArtActivityParticipants artActivityParticipants = artActivityParticipantsRepository.findArtActivityParticipantsByUserIdAndArtActivityId(userId, artActivity.getId());
            BeanUtils.copyProperties(artActivity, artActivityVO);
            artActivityVO.setContent(null);
            if (artActivityParticipants != null) {
                artActivityVO.setIsJoin(1);
            } else {
                artActivityVO.setIsJoin(0);
            }
            artActivityVOList.add(artActivityVO);
        }
        return Result.newSuccess(artActivityVOList);
    }

    @Override
    public Result<PageVO<DailySentence>> listAllDailySentence(PageArg arg) {
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<DailySentence> dailySentencePage = dailySentenceRepository.findAll(pageable);
        //构建pageVo对象
        PageVO<DailySentence> pageVo = PageVO.<DailySentence>builder()
                .totalPage(dailySentencePage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(dailySentencePage.getContent())
                .build();
        return Result.newSuccess(pageVo);
    }

    /**
     * 报名参加活动
     **/
    @Override
    public Result<Void> applyActivity(Integer userId, Integer artActivityId) {
        if (userId == null || artActivityId == null) {
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        User user = userRepository.findUserById(userId);
        ArtActivityParticipants artActivityParticipants = artActivityParticipantsRepository.findArtActivityParticipantsByUserIdAndArtActivityId(userId, artActivityId);
        ArtActivity artActivity = artActivityRepository.findArtActivityById(artActivityId);
        if (user == null) {
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        if (artActivity == null || artActivity.getIsEnd() == 1) {
            return Result.newError("活动不存在或已经结束");
        }
        if (artActivityParticipants != null) {
            return Result.newError("该用户已报名，无需重复报名");
        }
        artActivity.setParticipants(artActivity.getParticipants() + 1);
        ArtActivityParticipants a = new ArtActivityParticipants();
        a.setArtActivityId(artActivityId);
        a.setUserId(userId);
        a.setCreateTime(new Date());
        artActivityRepository.save(artActivity);
        artActivityParticipantsRepository.save(a);
        return Result.newSuccess();
    }

    /**
     * 为活动点赞
     **/
    @Override
    public Result<Void> likeActivity(Integer artActivityId) {
        ArtActivity artActivity = artActivityRepository.findArtActivityById(artActivityId);
        if (artActivity == null) {
            return Result.newError("活动不存在");
        }
        artActivity.setLikes(artActivity.getLikes() + 1);
        artActivityRepository.save(artActivity);
        return Result.newSuccess();
    }

    /**
     * 为活动取消点赞
     **/
    @Override
    public Result<Void> unlikeActivity(Integer artActivityId) {
        ArtActivity artActivity = artActivityRepository.findArtActivityById(artActivityId);
        if (artActivity == null) {
            return Result.newError("活动不存在");
        }
        artActivity.setLikes(artActivity.getLikes() - 1);
        artActivityRepository.save(artActivity);
        return Result.newSuccess();
    }
}
