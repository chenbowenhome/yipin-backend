package com.yipin.basic.VO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class RankingVO implements Serializable {
    private Integer id;
    /**
     * 周期数
     */
    private Integer period;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 截止时间
     */
    private Date endTime;

    List<RankingUserVO> rankingUserVOList;
}
