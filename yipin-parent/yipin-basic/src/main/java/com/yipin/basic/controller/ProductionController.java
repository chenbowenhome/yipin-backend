package com.yipin.basic.controller;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.ProductionVO;
import com.yipin.basic.entity.production.Production;
import com.yipin.basic.entity.production.ProductionTag;
import com.yipin.basic.entity.user.User;
import com.yipin.basic.form.ProductionForm;
import com.yipin.basic.service.ProductionService;
import enums.ResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "作品相关接口(首页、作品详情页面、个人页面)")
@RestController
@RequestMapping("/production")
public class ProductionController {

    @Autowired
    private ProductionService productionService;

    /**分页查询代表作**/
    @ApiOperation("首页分页查询用户发布的作品，按上传时间排序(首页)")
    @RequestMapping(value = "/listPublishedProduction",method = RequestMethod.POST)
    public Result<PageVO<ProductionVO>> listPublishedProduction(@RequestBody PageArg arg){
        arg.validate();
        return productionService.listPublishedProduction(arg);
    }

    /**上传作品信息,可选择保存或者直接发布**/
    @ApiOperation("上传作品信息,可选择不公开或者公开(上传作品页面)")
    @RequestMapping(value = "/uploadProduction",method = RequestMethod.POST)
    public Result<Void> uploadProduction(@Valid @RequestBody ProductionForm productionForm){
        return productionService.uploadProduction(productionForm);
    }

    /**将未公开的作品公开**/
    @ApiOperation("将未公开的作品公开(传入作品id)")
    @RequestMapping(value = "/publishProduction",method = RequestMethod.POST)
    public Result<Void> publishProduction(Integer id){
        return productionService.publishProduction(id);
    }

    /**将公开的作品设置为隐私**/
    @ApiOperation("将公开的作品设置为隐私(传入作品id)")
    @RequestMapping(value = "/privateProduction",method = RequestMethod.POST)
    public Result<Void> privateProduction(Integer id) {
        return productionService.privateProduction(id);
    }

    /**分页查询用户已公开的全部作品**/
    @ApiOperation("分页查询用户已公开的全部作品(用户个人页面公开作品模块)")
    @RequestMapping(value = "/listUserPublishedProduction",method = RequestMethod.POST)
    public Result<PageVO<ProductionVO>> listUserPublishedProduction(@RequestBody PageArg arg,Integer userId){
        arg.validate();
        return productionService.listUserPublishedProduction(arg,userId);
    }

    /**分页查询用户还没公开的作品**/
    @ApiOperation("分页查询用户还没公开的作品(用户个人页面私密作品模块)")
    @RequestMapping(value = "/listUserUnPublishedProduction",method = RequestMethod.POST)
    public Result<PageVO<ProductionVO>> listUserUnPublishedProduction(@RequestBody PageArg arg,Integer userId){
        arg.validate();
        return productionService.listUserUnPublishedProduction(arg,userId);
    }

    /**为作品点赞**/
    @ApiOperation("为作品点赞,传入作品id和点赞用户id")
    @RequestMapping(value = "/likes",method = RequestMethod.POST)
    public Result<Void> likes(Integer id,Integer userId){
        return productionService.likes(id,userId);
    }

    /**取消点赞**/
    @ApiOperation("取消点赞,传入作品id和用户id")
    @RequestMapping(value = "/unlikes",method = RequestMethod.POST)
    public Result<Void> unlikes(Integer id,Integer userId){
        return productionService.unlikes(id,userId);
    }

    /**浏览作品**/
    @ApiOperation("浏览作品,传入作品id")
    @RequestMapping(value = "/views",method = RequestMethod.POST)
    public Result<Void> views(Integer id){
        return productionService.views(id);
    }

    /**获取用户代表作**/
    @ApiOperation("获取用户代表作,传入用户id(个人页面)")
    @RequestMapping(value = "/getMainProduction",method = RequestMethod.GET)
    public Result<List<ProductionVO>> getMainProduction(Integer userId){
        return productionService.getMainProduction(userId);
    }

    /**根据id获取作品信息**/
    @ApiOperation("根据id获取作品信息")
    @RequestMapping(value = "/getProductionById",method = RequestMethod.POST)
    public Result<ProductionVO> getProductionById(Integer id,Integer userId) {
        return productionService.getProductionById(id,userId);
    }

    /**获取所有分类标签**/
    @ApiOperation("获取所有分类标签")
    @RequestMapping(value = "/listProductionTags",method = RequestMethod.GET)
    public Result<List<ProductionTag>> listProductionTags() {
        return productionService.listProductionTags();
    }

    /**根据作品标题或者描述查询作品**/
    @ApiOperation("根据作品标题或者描述查询作品")
    @RequestMapping(value = "/findProductionByKey",method = RequestMethod.POST)
    public Result<PageVO<ProductionVO>> findProductionByKey(String key,@RequestBody PageArg arg) {
        arg.validate();
        return productionService.findProductionByKey(key,arg);
    }

    /**
     * 删除用户自己的作品
     **/
    @ApiOperation("删除用户自己的作品(传入用户id和作品id)")
    @RequestMapping(value = "/deleteProductionById",method = RequestMethod.POST)
    public Result<Void> deleteProductionById(Integer userId, Integer productionId) {
        return productionService.deleteProductionById(userId,productionId);
    }

    /**
     * 取消代表作
     **/
    @ApiOperation("取消代表作")
    @RequestMapping(value = "/cancelMainProduction",method = RequestMethod.POST)
    public Result<Void> cancelMainProduction(Integer userId, Integer productionId) {
        return productionService.cancelMainProduction(userId,productionId);
    }
}
