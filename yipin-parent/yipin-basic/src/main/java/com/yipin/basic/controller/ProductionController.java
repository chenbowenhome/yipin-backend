package com.yipin.basic.controller;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.ProductionVO;
import com.yipin.basic.form.ProductionForm;
import com.yipin.basic.service.ProductionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "作品相关接口")
@RestController
@RequestMapping("/production")
public class ProductionController {

    @Autowired
    private ProductionService productionService;

    /**分页查询代表作**/
    @ApiOperation("首页分页查询代表作，按品值排序")
    @RequestMapping(value = "/listPublishedProduction",method = RequestMethod.POST)
    public Result<PageVO<ProductionVO>> listPublishedProduction(@RequestBody PageArg arg){
        arg.validate();
        return productionService.listPublishedProduction(arg);
    }

    /**上传作品信息,可选择保存或者直接发布**/
    @ApiOperation("上传作品信息,可选择不公开或者公开")
    @RequestMapping(value = "/uploadProduct ion",method = RequestMethod.POST)
    public Result<Void> uploadProduction(@Valid @RequestBody ProductionForm productionForm){
        return productionService.uploadProduction(productionForm);
    }

    /**将未公开的作品公开**/
    @ApiOperation("将未公开的作品公开")
    @RequestMapping(value = "/publishProduction",method = RequestMethod.POST)
    public Result<Void> publishProduction(Integer id){
        return productionService.publishProduction(id);
    }

    /**将公开的作品设置为隐私**/
    @ApiOperation("将公开的作品设置为隐私")
    @RequestMapping(value = "/privateProduction",method = RequestMethod.POST)
    public Result<Void> privateProduction(Integer id) {
        return productionService.privateProduction(id);
    }

    /**分页查询用户已公开的全部作品**/
    @ApiOperation("分页查询用户已公开的全部作品")
    @RequestMapping(value = "/listUserPublishedProduction",method = RequestMethod.POST)
    public Result<PageVO<ProductionVO>> listUserPublishedProduction(@RequestBody PageArg arg,Integer userId){
        arg.validate();
        return productionService.listUserPublishedProduction(arg,userId);
    }

    /**分页查询用户还没公开的作品**/
    @ApiOperation("分页查询用户还没公开的作品")
    @RequestMapping(value = "/listUserUnPublishedProduction",method = RequestMethod.POST)
    public Result<PageVO<ProductionVO>> listUserUnPublishedProduction(@RequestBody PageArg arg,Integer userId){
        arg.validate();
        return productionService.listUserUnPublishedProduction(arg,userId);
    }

    /**为作品点赞**/
    @ApiOperation("为作品点赞,传入作品id")
    @RequestMapping(value = "/likes",method = RequestMethod.POST)
    public Result<Void> likes(Integer id){
        return productionService.likes(id);
    }

    /**浏览作品**/
    @ApiOperation("浏览作品,传入作品id")
    @RequestMapping(value = "/views",method = RequestMethod.POST)
    public Result<Void> views(Integer id){
        return productionService.views(id);
    }

    /**获取用户代表作**/
    @ApiOperation("获取用户代表作,传入用户id")
    @RequestMapping(value = "/getMainProduction",method = RequestMethod.POST)
    public Result<ProductionVO> getMainProduction(Integer userId){
        return productionService.getMainProduction(userId);
    }

    /**根据标题搜索作品**/
    @ApiOperation("根据标题搜索作品")
    @RequestMapping(value = "/searchProduction",method = RequestMethod.POST)
    public Result<PageVO<ProductionVO>> searchProduction(String title,@RequestBody PageArg arg){
        arg.validate();
        return productionService.searchProduction(title,arg);
    }
}