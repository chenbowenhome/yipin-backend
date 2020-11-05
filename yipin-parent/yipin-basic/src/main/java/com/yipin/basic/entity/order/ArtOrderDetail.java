package com.yipin.basic.entity.order;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
    private String artProductId;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 支付总金额
     */
    private BigDecimal payAmount;

    private Date createTime;

    private Date updateTime;
    /**
     * 支付类型
     */
    private Integer payType;
    /**
     * 被打赏的人的id
     */
    private Integer rewardUserId;
    /**
     * 此次订单的id
     */
    private String orderId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品价格
     */
    private BigDecimal productPrice;
    /**
     * 商品数量
     */
    private Integer productAmount;
}
