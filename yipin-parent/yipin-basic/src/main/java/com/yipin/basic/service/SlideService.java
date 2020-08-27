package com.yipin.basic.service;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.entity.Slide;
import com.yipin.basic.form.SlideForm;

public interface SlideService {
    /**新增一个轮播图**/
    Result<Void> addSlide(SlideForm slideForm);
    /**删除一个轮播图**/
    Result<Void> deleteSlide(Integer id);
    /**分页获取轮播图信息**/
    Result<PageVO<Slide>> listSlide(PageArg arg);
    /**更新一个轮播图**/
    Result<Void> updateSlide(Integer id,SlideForm slideForm);
}
