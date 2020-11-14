package com.yipin.basic.VO;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ShoppingCartVO implements Serializable {

    private Integer id;
    /**
     * 商品所属id，同样的商品不同价格有着相同的所属id
     */
    private String artProductId;
    /**
     * 商品数量
     */
    private Integer productNums;

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
     * 支付类型
     */
    private Integer payType;
    /**
     * 商品库存
     */
    private Integer productRepertory;
    /**
     * 商品类型，0为实体商品（有库存），1为虚拟类商品（无库存）
     */
    private Integer productType;
    /**
     * 商品小图
     */
    private String productImg;
}
