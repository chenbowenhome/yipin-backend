package com.yipin.basic.service.impl;

import VO.PageVO;
import VO.Result;
import args.PageArg;
import com.yipin.basic.VO.MasterAvatarVO;
import com.yipin.basic.VO.ProductionVO;
import com.yipin.basic.dao.master.MasterProductionRepository;
import com.yipin.basic.dao.master.MasterRepository;
import com.yipin.basic.entity.master.Master;
import com.yipin.basic.entity.master.MasterProduction;
import com.yipin.basic.service.MasterService;
import enums.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MasterServiceImpl implements MasterService {

    @Autowired
    private MasterRepository masterRepository;
    @Autowired
    private MasterProductionRepository masterProductionRepository;

    /**获取10个大师的信息**/
    @Override
    public Result<List<MasterAvatarVO>> listTopTenMaster() {
        List<Master> masterList = masterRepository.getTopTenMaster();
        List<MasterAvatarVO> masterAvatarVOList = new ArrayList<>();
        for (Master master : masterList) {
            MasterAvatarVO masterAvatarVO = new MasterAvatarVO();
            masterAvatarVO.setId(master.getId());
            masterAvatarVO.setMasterAvatar(master.getMasterAvatar());
            masterAvatarVOList.add(masterAvatarVO);
        }
        return Result.newSuccess(masterAvatarVOList);
    }

    /**根据大师id获取大师详细信息(大师详情)**/
    @Override
    public Result<Master> getMasterById(Integer id) {
        if (id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Master master = masterRepository.findMasterById(id);
        if (master == null){
            return Result.newError("大师不存在");
        }
        return Result.newSuccess(master);
    }

    /**根据大师id获取大师的全部作品(大师展厅)**/
    @Override
    public Result<PageVO<MasterProduction>> listMasterProduction(Integer masterId, PageArg arg) {
        if (masterId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<MasterProduction> masterProductions = masterProductionRepository.findAllByMasterId(masterId,pageable);
        //构建pageVo对象
        PageVO<MasterProduction> pageVo = PageVO.<MasterProduction>builder()
                .totalPage(masterProductions.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(masterProductions.getContent())
                .build();
        return Result.newSuccess(pageVo);
    }
}
