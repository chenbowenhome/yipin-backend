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

@ApiModel(value = "订单详情表")
@Data
@Entity
@Table(name = "art_order_detail")
public class ArtOrderDetail implements Serializable {
    /**
     * ID
     */
    @Id
    private String id;
    /**
     * 商品id，非商品所属id
     */
    @ApiModelProperty("商品id，非商品所属id")
    private String artProductId;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Integer userId;
    /**
     * 支付总金额
     */
    @ApiModelProperty("支付总金额")
    private BigDecimal payAmount;

    private Date createTime;

    private Date updateTime;
    /**
     * 支付类型
     */
    @ApiModelProperty("支付类型,0为付钱给我们系统，1为打赏给其他用户，2为用虚拟货币支付")
    private Integer payType;
    /**
     * 被打赏的人的id
     */
    @ApiModelProperty("被打赏的人的id,不是打赏则为空")
    private Integer rewardUserId;
    /**
     * 此次订单的id
     */
    @ApiModelProperty("此次订单的id")
    private String orderId;
    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String productName;
    /**
     * 商品价格
     */
    @ApiModelProperty("商品价格")
    private BigDecimal productPrice;
    /**
     * 商品数量
     */
    @ApiModelProperty("商品数量")
    private Integer productAmount;
}
