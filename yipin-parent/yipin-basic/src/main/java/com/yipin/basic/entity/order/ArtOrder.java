package com.yipin.basic.entity.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "总订单表")
@Data
@Entity
@Table(name = "art_order")
public class ArtOrder implements Serializable {
    /**
     * ID
     */
    @Id
    private String id;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Integer userId;
    /**
     * 订单总金额
     */
    @ApiModelProperty("订单总金额")
    private BigDecimal money;
    /**
     * 商户名称
     */
    @ApiModelProperty("商户名称")
    private String name;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
     * 订单状态 0为新下单 1为订单完结 2为订单失败
     */
    @ApiModelProperty("订单状态 0为新下单 1为订单完结 2为订单失败")
    private Integer orderStatus;
    /**
     * 支付状态 0为未支付 1为支付成功 2为支付失败
     */
    @ApiModelProperty("支付状态 0为未支付 1为支付成功 2为支付失败")
    private Integer payStatus;
}
