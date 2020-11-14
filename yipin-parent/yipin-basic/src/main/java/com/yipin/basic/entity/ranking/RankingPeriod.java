package com.yipin.basic.entity.ranking;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "用户排名期数表")
@Data
@Entity
@Table(name = "ranking_period")
public class RankingPeriod implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 周期数
     */
    @ApiModelProperty("周期数")
    private Integer period;
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date startTime;
    /**
     * 截止时间
     */
    @ApiModelProperty("截止时间")
    private Date endTime;
}
