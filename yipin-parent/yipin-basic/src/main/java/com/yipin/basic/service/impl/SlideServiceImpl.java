package com.yipin.basic.service.impl;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.UserVO;
import com.yipin.basic.dao.SlideRepository;
import com.yipin.basic.entity.Slide;
import com.yipin.basic.form.SlideForm;
import com.yipin.basic.service.SlideService;
import enums.ResultEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SlideServiceImpl implements SlideService {
    @Autowired
    private SlideRepository slideRepository;

    /**新增一个轮播图**/
    @Override
    public Result<Void> addSlide(SlideForm slideForm) {
        Slide slide = new Slide();
        BeanUtils.copyProperties(slideForm,slide);
        slideRepository.save(slide);
        return Result.newSuccess();
    }

    /**删除一个轮播图**/
    @Override
    public Result<Void> deleteSlide(Integer id) {
        Slide slide = slideRepository.findSlideById(id);
        if (slide == null){
            return Result.newError("轮播图不存在");
        }
        slideRepository.deleteById(id);
        return Result.newSuccess();
    }

    /**分页获取轮播图信息**/
    @Override
    public Result<PageVO<Slide>> listSlide(PageArg arg) {
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<Slide> slidePage = slideRepository.findAllByOrderByOrderNumDesc(pageable);
        List<Slide> slideList = slidePage.getContent();
        //构建pageVo对象
        PageVO<Slide> pageVo = PageVO.<Slide>builder()
                .totalPage(slidePage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(slideList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**更新一个轮播图**/
    @Override
    public Result<Void> updateSlide(Integer id, SlideForm slideForm) {
        if (id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Slide slide = slideRepository.findSlideById(id);
        if (slide == null){
            return Result.newError("轮播图不存在");
        }
        BeanUtils.copyProperties(slideForm,slide);
        slideRepository.save(slide);
        return Result.newSuccess();
    }
}
