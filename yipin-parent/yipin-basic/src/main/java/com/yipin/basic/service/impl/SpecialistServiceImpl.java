package com.yipin.basic.service.impl;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.ProductionVO;
import com.yipin.basic.VO.SpecialistVO;
import com.yipin.basic.dao.specialistDao.SpecialistRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.specialist.Specialist;
import com.yipin.basic.entity.user.User;
import com.yipin.basic.form.SpecialistForm;
import com.yipin.basic.service.SpecialistService;
import enums.ResultEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SpecialistServiceImpl implements SpecialistService {

    @Autowired
    private SpecialistRepository specialistRepository;
    @Autowired
    private UserRepository userRepository;

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

    /**获取专家列表**/
    @Override
    public Result<PageVO<Specialist>> listSpecialist(PageArg arg) {
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<Specialist> specialistPage = specialistRepository.findAll(pageable);
        //构建pageVo对象
        PageVO<Specialist> pageVo = PageVO.<Specialist>builder()
                .totalPage(specialistPage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(specialistPage.getContent())
                .build();
        return Result.newSuccess(pageVo);
    }

    /**获取前十名专家用户头像**/
    @Override
    public Result<List<SpecialistVO>> listTopSpecialist() {
        List<User> userList = userRepository.findSpecialist();
        List<SpecialistVO> specialistVOList = new ArrayList<>();
        for (User user : userList) {
            Specialist specialist = specialistRepository.findSpecialistByUserId(user.getId());
            SpecialistVO specialistVO = new SpecialistVO();
            specialistVO.setId(specialist.getId());
            specialistVO.setUserId(user.getId());
            specialistVO.setAvatar(user.getAvatar());
            specialistVOList.add(specialistVO);
        }
        return Result.newSuccess(specialistVOList);
    }

    /**获取全部专家用户头像**/
    @Override
    public Result<PageVO<SpecialistVO>> listAllSpecialist(PageArg arg) {
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<User> userPage = userRepository.findSpecialistByPage(pageable);
        List<User> userList = userPage.getContent();
        List<SpecialistVO> specialistVOList = new ArrayList<>();
        for (User user : userList) {
            Specialist specialist = specialistRepository.findSpecialistByUserId(user.getId());
            SpecialistVO specialistVO = new SpecialistVO();
            specialistVO.setId(specialist.getId());
            specialistVO.setUserId(user.getId());
            specialistVO.setAvatar(user.getAvatar());
            specialistVOList.add(specialistVO);
        }
        //构建pageVo对象
        PageVO<SpecialistVO> pageVo = PageVO.<SpecialistVO>builder()
                .totalPage(userPage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(specialistVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**获取到专家信息**/
    @Override
    public Result<Specialist> getSpecialist(Integer id) {
        if (id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Specialist specialist = specialistRepository.findSpecialistById(id);
        if (specialist == null){
            return Result.newError("专家不存在");
        }
        return Result.newSuccess(specialist);
    }
}
