package com.yipin.basic.service;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.entity.specialist.Specialist;
import com.yipin.basic.form.SpecialistForm;

public interface SpecialistService {
    /**申请成为专家**/
    Result<Void> applyToSpecialist(SpecialistForm specialistForm);
    /**获取专家列表**/
    Result<PageVO<Specialist>> listSpecialist(PageArg arg);
}
