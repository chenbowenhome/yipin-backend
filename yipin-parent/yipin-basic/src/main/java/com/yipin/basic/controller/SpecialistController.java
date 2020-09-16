package com.yipin.basic.controller;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.entity.specialist.Specialist;
import com.yipin.basic.form.SpecialistForm;
import com.yipin.basic.service.SpecialistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "专家相关接口")
@RestController
@RequestMapping("/specialist")
public class SpecialistController {

    @Autowired
    private SpecialistService specialistService;


    /**申请成为专家**/
    @ApiOperation("申请成为专家")
    @RequestMapping(value = "/applyToSpecialist",method = RequestMethod.POST)
    public Result<Void> applyToSpecialist(@Valid @RequestBody SpecialistForm specialistForm) {
        return specialistService.applyToSpecialist(specialistForm);
    }

    /**获取专家列表**/
    @ApiOperation("获取专家列表")
    @RequestMapping(value = "/listSpecialist",method = RequestMethod.POST)
    public Result<PageVO<Specialist>> listSpecialist(@RequestBody PageArg arg) {
        arg.validate();
        return specialistService.listSpecialist(arg);
    }
}
