package com.yipin.basic.service;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.MsgVO;


public interface MsgService {
    /**分页获取全部消息**/
    Result<PageVO<MsgVO>> listAllMsg(Integer userId, PageArg arg);
    /**获取未读消息数目**/
    Result<Integer> getNotViewNum(Integer userId);
    /**删除消息**/
    Result<Void> deleteMsg(Integer id);
}
