package com.yipin.basic.service;

import VO.PageVO;
import VO.Result;
import args.PageArg;
import com.yipin.basic.VO.MasterAvatarVO;
import com.yipin.basic.entity.master.Master;
import com.yipin.basic.entity.master.MasterProduction;

import java.util.List;

public interface MasterService {
    /**获取10个大师的信息**/
    Result<List<MasterAvatarVO>> listTopTenMaster();
    /**根据大师id获取大师详细信息(大师详情)**/
    Result<Master> getMasterById(Integer id);
    /**根据大师id获取大师的全部作品(大师展厅)**/
    Result<PageVO<MasterProduction>> listMasterProduction(Integer masterId, PageArg arg);
}
