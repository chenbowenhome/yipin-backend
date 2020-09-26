package com.yipin.basic.entity.order;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "art_product")
public class ArtProduct implements Serializable {
    /**
     * ID
     */
    @Id
    private String id;
    /**
     * 商品所属id，同样的商品不同价格有着相同的所属id
     */
    private String productId;
    /**
     * 商品名字
     */
    private String productName;
    /**
     * 商品描述
     */
    private String productDescription;
    /**
     * 商品价格
     */
    private BigDecimal productPrice;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 支付类型
     */
    private Integer payType;
}
