package com.yipin.basic.VO;

import com.yipin.basic.entity.ranking.RankingPeriod;
import com.yipin.basic.entity.user.UserArt;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class RankingUserVO implements Serializable {
    /**
     * ID
     */
    private Integer id;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 品值
     */
    private BigDecimal performanceNum;
    /**
     * 本期排名
     */
    private Integer ranking;

    private UserArt userArt;

    private RankingPeriod rankingPeriod;
}
