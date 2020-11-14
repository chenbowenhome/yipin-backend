package com.yipin.basic.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "用户购物车表")
@Data
@Entity
@Table(name = "user_product_cart")
public class UserProductCart implements Serializable {
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
     * 商品id
     */
    @ApiModelProperty("商品id")
    private String artProductId;
    /**
     * 商品数量
     */
    @ApiModelProperty("商品数量")
    private Integer productNums;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否完结
     */
    @ApiModelProperty("是否完结")
    private Integer isEnd;
}
