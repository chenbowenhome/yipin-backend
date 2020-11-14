package com.yipin.basic.entity.others;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "活动分享表")
@Data
@Entity
@Table(name = "art_activity_transpond")
public class ArtActivityTranspond implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 第一个分享的用户id
     */
    @ApiModelProperty("第一个分享的用户id")
    private Integer firstUserId;
    /**
     * 第二个分享的用户id
     */
    @ApiModelProperty("第二个分享的用户id")
    private Integer secondUserId;
    /**
     * 第一次分享的金额
     */
    @ApiModelProperty("第一次分享的金额")
    private BigDecimal firstMoney;
    /**
     * 第二次分享的金额
     */
    @ApiModelProperty("第二次分享的金额")
    private BigDecimal secondMoney;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否完结，0为未完结，1为已完结，默认为未完结
     */
    @ApiModelProperty("是否完结，0为未完结，1为已完结，默认为未完结")
    private Integer isEnd;
}
