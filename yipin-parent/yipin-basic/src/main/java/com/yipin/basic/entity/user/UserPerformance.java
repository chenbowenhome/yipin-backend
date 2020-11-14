package com.yipin.basic.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "用户品值参数表")
@Data
@Entity
@Table(name = "user_performance")
public class UserPerformance {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户本周期上传作品数
     */
    @ApiModelProperty("用户本周期上传作品数")
    private Integer uploadProductionNums;
    /**
     * 周期天数
     */
    @ApiModelProperty("周期天数")
    private Integer periodDays;
    /**
     * 本周评价他人的次数
     */
    @ApiModelProperty("本周评价他人的次数")
    private Integer commentNums;
    /**
     * 本周点赞次数
     */
    @ApiModelProperty("本周点赞次数")
    private Integer likeNums;
    /**
     * 本周打赏他人的金额
     */
    @ApiModelProperty("本周打赏他人的金额")
    private BigDecimal exceptionalAmount;
    /**
     * 本周被他人打赏的金额
     */
    @ApiModelProperty("本周被他人打赏的金额")
    private BigDecimal rewardAmount;
    /**
     * 坚持度
     */
    @ApiModelProperty("坚持度")
    private BigDecimal insistence;
    /**
     * 勤奋度
     */
    @ApiModelProperty("勤奋度")
    private BigDecimal assiduous;
    /**
     * 参与度
     */
    @ApiModelProperty("参与度")
    private BigDecimal participation;
    /**
     * 价值度
     */
    @ApiModelProperty("价值度")
    private BigDecimal worth;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否为周期内
     */
    @ApiModelProperty("是否为周期内")
    private Integer isPeriod;
}
