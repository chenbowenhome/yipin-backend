package com.yipin.basic.service.impl;

import VO.Result;
import VO.Void;
import com.yipin.basic.dao.specialistDao.SpecialistRepository;
import com.yipin.basic.entity.specialist.Specialist;
import com.yipin.basic.form.SpecialistForm;
import com.yipin.basic.service.SpecialistService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SpecialistServiceImpl implements SpecialistService {

    @Autowired
    private SpecialistRepository specialistRepository;

    /**申请成为专家**/
    @Override
    public Result<Void> applyToSpecialist(SpecialistForm specialistForm) {
        List<Specialist> specialistList = specialistRepository.findSpecialistByCheckStatus(specialistForm.getUserId());
        if (!specialistList.isEmpty()){
            return Result.newError("已经申请专家，无需重复申请");
        }
        Specialist specialist = new Specialist();
        BeanUtils.copyProperties(specialistForm,specialist);
        specialist.setCheckStatus(0);
        specialist.setCreateTime(new Date());
        specialist.setUpdateTime(new Date());
        specialistRepository.save(specialist);
        return Result.newSuccess();
    }
}
