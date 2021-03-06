package com.yipin.basic.entity.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "商品表")
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
    @ApiModelProperty("商品所属id，同样的商品不同价格有着相同的所属id")
    private String productId;
    /**
     * 商品名字
     */
    @ApiModelProperty("商品名字")
    private String productName;
    /**
     * 商品描述
     */
    @ApiModelProperty("商品描述")
    private String productDescription;
    /**
     * 商品价格
     */
    @ApiModelProperty("商品价格")
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
    @ApiModelProperty("支付类型")
    private Integer payType;
    /**
     * 商品库存
     */
    @ApiModelProperty("商品库存")
    private Integer productRepertory;
    /**
     * 商品类型，0为实体商品（有库存），1为虚拟类商品（无库存）
     */
    @ApiModelProperty("商品类型，0为实体商品（有库存），1为虚拟类商品（无库存）")
    private Integer productType;
    /**
     * 商品小图
     */
    @ApiModelProperty("商品小图")
    private String productImg;
}
