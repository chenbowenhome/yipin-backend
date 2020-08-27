package com.yipin.basic.service;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.ProductionVO;
import com.yipin.basic.entity.production.Production;
import com.yipin.basic.form.ProductionForm;

public interface ProductionService {
    /**分页查询代表作品**/
    Result<PageVO<ProductionVO>> listPublishedProduction(PageArg arg);
    /**上传作品信息,可选择不公开或者公开**/
    Result<Void> uploadProduction(ProductionForm productionForm);
    /**将未公开的作品公开**/
    Result<Void> publishProduction(Integer id);
    /**将公开的作品设置为隐私**/
    Result<Void> privateProduction(Integer id);
    /**分页查询用户已公开的全部作品**/
    Result<PageVO<ProductionVO>> listUserPublishedProduction(PageArg arg,Integer userId);
    /**分页查询用户还没公开的作品**/
    Result<PageVO<ProductionVO>> listUserUnPublishedProduction(PageArg arg,Integer userId);
    /**为作品点赞**/
    Result<Void> likes(Integer id);
    /**浏览作品**/
    Result<Void> views(Integer id);
    /**获取用户代表作**/
    Result<ProductionVO> getMainProduction(Integer userId);
    /**根据标题搜索作品**/
    Result<PageVO<ProductionVO>> searchProduction(String title,PageArg arg);
}