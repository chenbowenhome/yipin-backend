package com.yipin.basic.entity.user;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

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
     * 用户上传作品总次数
     */
    private Integer allUploadNums;
    /**
     * 用户本周期上传作品数
     */
    private Integer uploadProductionNums;
    /**
     * 周期天数
     */
    private Integer periodDays;
    /**
     * 本周评价他人的次数
     */
    private Integer commentNums;
    /**
     * 本周被他人评价的次数
     */
    private Integer beCommentedNums;
    /**
     * 本周打赏他人的金额
     */
    private BigDecimal exceptionalAmount;
    /**
     * 本周被他人打赏的金额
     */
    private BigDecimal rewardAmount;
    /**
     * 坚持度
     */
    private BigDecimal insistence;
    /**
     * 勤奋度
     */
    private BigDecimal assiduous;
    /**
     * 参与度
     */
    private BigDecimal participation;
    /**
     * 价值度
     */
    private BigDecimal worth;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
