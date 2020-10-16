package com.yipin.basic.service.impl;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.CommentVO;
import com.yipin.basic.VO.MsgVO;
import com.yipin.basic.dao.othersDao.ArtMsgRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.others.ArtMsg;
import com.yipin.basic.entity.user.User;
import com.yipin.basic.service.MsgService;
import enums.ResultEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MsgServiceImpl implements MsgService {
    @Autowired
    private ArtMsgRepository msgRepository;
    @Autowired
    private UserRepository userRepository;

    /**分页获取全部消息**/
    @Override
    public Result<PageVO<MsgVO>> listAllMsg(Integer userId, PageArg arg) {
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<ArtMsg> artMsgPage = msgRepository.findArtMsgByReceiveUserIdOrderByCreateTimeDesc(userId,pageable);
        List<ArtMsg> artMsgList = artMsgPage.getContent();
        List<MsgVO> msgVOList = new ArrayList<>();
        for (ArtMsg artMsg : artMsgList) {
            User user = userRepository.findUserById(artMsg.getUserId());
            MsgVO msgVO = new MsgVO();
            BeanUtils.copyProperties(artMsg,msgVO);
            msgVO.setNickname(user.getNickname());
            msgVO.setAvatar(user.getAvatar());
            msgVOList.add(msgVO);
        }
        List<ArtMsg> artMsgs = msgRepository.findArtMsgByViewStatusAndReceiveUserId(0,userId);
        for (ArtMsg artMsg : artMsgs) {
            artMsg.setViewStatus(1);
            msgRepository.save(artMsg);
        }
        PageVO<MsgVO> pageVo = PageVO.<MsgVO>builder()
                .totalPage(artMsgPage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(msgVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**获取未读消息数目**/
    @Override
    public Result<Integer> getNotViewNum(Integer userId) {
        Integer num = msgRepository.findArtMsgByViewStatusAndReceiveUserId(0,userId).size();
        return Result.newSuccess(num);
    }

    /**删除消息**/
    @Override
    public Result<Void> deleteMsg(Integer id) {
        if (id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        ArtMsg artMsg = msgRepository.findArtMsgById(id);
        if (artMsg == null){
            return Result.newError("消息不存在");
        }
        msgRepository.delete(artMsg);
        return Result.newSuccess();
    }
}
