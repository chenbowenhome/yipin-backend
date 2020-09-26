package com.yipin.basic.controller;

import VO.PageVO;
import VO.Result;
import args.PageArg;
import com.yipin.basic.VO.MsgVO;
import com.yipin.basic.entity.others.ArtMsg;
import com.yipin.basic.service.MsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "消息相关接口")
@RestController
@RequestMapping("/msg")
public class MsgController {
    @Autowired
    private MsgService msgService;


    /**分页获取全部消息**/
    @ApiOperation("分页获取全部消息")
    @RequestMapping(value = "/listAllMsg",method = RequestMethod.POST)
    public Result<PageVO<MsgVO>> listAllMsg(Integer userId, @RequestBody PageArg arg) {
        arg.validate();
        return msgService.listAllMsg(userId,arg);
    }

    /**获取未读消息数目**/
    @ApiOperation("获取未读消息数目")
    @RequestMapping(value = "/getNotViewNum",method = RequestMethod.GET)
    public Result<Integer> getNotViewNum(Integer userId) {
        return msgService.getNotViewNum(userId);
    }
}
