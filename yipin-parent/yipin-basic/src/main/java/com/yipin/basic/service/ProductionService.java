package com.yipin.basic.service;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.ProductionVO;
import com.yipin.basic.entity.production.Production;
import com.yipin.basic.entity.production.ProductionTag;
import com.yipin.basic.form.ProductionForm;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
    Result<Void> likes(Integer id,Integer userId);
    /**取消点赞**/
    Result<Void> unlikes(Integer id,Integer userId);
    /**浏览作品**/
    Result<Void> views(Integer id);
    /**获取用户代表作**/
    Result<List<ProductionVO>> getMainProduction(Integer userId);
    /**根据标题搜索作品**/
    Result<PageVO<ProductionVO>> searchProduction(String title,PageArg arg);
    /**根据id获取作品信息**/
    Result<ProductionVO> getProductionById(Integer id,Integer userId);
    /**获取所有分类标签**/
    Result<List<ProductionTag>> listProductionTags();
    /**根据作品标题或者描述查询作品**/
    Result<PageVO<ProductionVO>> findProductionByKey(String key, PageArg arg);
    /**删除用户自己的作品**/
    Result<Void> deleteProductionById(Integer userId,Integer productionId);
    /**取消代表作**/
    Result<Void> cancelMainProduction(Integer userId,Integer productionId);
}
