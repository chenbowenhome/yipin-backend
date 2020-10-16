package com.yipin.basic.service;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.SpecialistVO;
import com.yipin.basic.entity.specialist.Specialist;
import com.yipin.basic.form.SpecialistForm;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SpecialistService {
    /**申请成为专家**/
    Result<Void> applyToSpecialist(SpecialistForm specialistForm);
    /**获取专家列表**/
    Result<PageVO<Specialist>> listSpecialist(PageArg arg);
    /**获取前十名专家用户头像**/
    Result<List<SpecialistVO>> listTopSpecialist();
    /**获取全部专家用户头像**/
    Result<PageVO<SpecialistVO>> listAllSpecialist(PageArg arg);
    /**获取到专家信息**/
    Result<Specialist> getSpecialist(Integer id);
}
