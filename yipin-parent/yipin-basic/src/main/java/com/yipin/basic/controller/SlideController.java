package com.yipin.basic.controller;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.entity.others.Slide;
import com.yipin.basic.form.SlideForm;
import com.yipin.basic.service.SlideService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "轮播图相关接口")
@RestController
@RequestMapping("/slide")
public class SlideController {
    @Autowired
    private SlideService slideService;

    /**新增一个轮播图**/
    @ApiOperation("新增一个轮播图")
    @RequestMapping(value = "/addSlide",method = RequestMethod.POST)
    public Result<Void> addSlide(@Valid @RequestBody SlideForm slideForm){
        return slideService.addSlide(slideForm);
    }
    /**删除一个轮播图**/
    @ApiOperation("删除一个轮播图")
    @RequestMapping(value = "/deleteSlide",method = RequestMethod.POST)
    public Result<Void> deleteSlide(Integer id){
        return slideService.deleteSlide(id);
    }
    /**分页获取轮播图信息**/
    @ApiOperation("分页获取轮播图信息")
    @RequestMapping(value = "/listSlide",method = RequestMethod.POST)
    public Result<PageVO<Slide>> listSlide(@RequestBody PageArg arg){
        arg.validate();
        return slideService.listSlide(arg);
    }
    /**更新一个轮播图**/
    @ApiOperation("更新一个轮播图")
    @RequestMapping(value = "/updateSlide",method = RequestMethod.POST)
    public Result<Void> updateSlide(Integer id,@Valid @RequestBody SlideForm slideForm){
        return slideService.updateSlide(id,slideForm);
    }
}
