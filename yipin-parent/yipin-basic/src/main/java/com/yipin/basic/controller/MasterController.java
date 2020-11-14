package com.yipin.basic.controller;

import VO.PageVO;
import VO.Result;
import args.PageArg;
import com.yipin.basic.VO.MasterAvatarVO;
import com.yipin.basic.entity.master.Master;
import com.yipin.basic.entity.master.MasterProduction;
import com.yipin.basic.service.MasterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "大师相关接口(发现页面的大师模块)")
@RestController
@RequestMapping("/master")
public class MasterController {

    @Autowired
    private MasterService masterService;

    /**
     * 获取10个大师的信息
     **/
    @ApiOperation("获取10个大师的信息,返回大师的头像和大师id")
    @RequestMapping(value = "/listTopTenMaster", method = RequestMethod.GET)
    public Result<List<MasterAvatarVO>> listTopTenMaster() {
        return masterService.listTopTenMaster();
    }

    /**
     * 根据大师id获取大师详细信息(大师详情)
     **/
    @ApiOperation("根据大师id获取大师详细信息(大师详情页面)")
    @RequestMapping(value = "/getMasterById", method = RequestMethod.POST)
    public Result<Master> getMasterById(Integer id) {
        return masterService.getMasterById(id);
    }

    /**
     * 根据大师id获取大师的全部作品(大师展厅)
     **/
    @ApiOperation("根据大师id获取大师的全部作品(大师展厅展厅页面)")
    @RequestMapping(value = "/listMasterProduction", method = RequestMethod.POST)
    public Result<PageVO<MasterProduction>> listMasterProduction(Integer masterId, @RequestBody PageArg arg) {
        arg.validate();
        return masterService.listMasterProduction(masterId, arg);
    }
}
