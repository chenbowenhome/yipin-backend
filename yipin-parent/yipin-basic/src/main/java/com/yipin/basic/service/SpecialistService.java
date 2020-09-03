package com.yipin.basic.service;

import VO.Result;
import VO.Void;
import com.yipin.basic.form.SpecialistForm;

public interface SpecialistService {
    /**申请成为专家**/
    Result<Void> applyToSpecialist(SpecialistForm specialistForm);

}
