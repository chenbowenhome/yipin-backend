package com.yipin.basic.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class OrderForm implements Serializable {
    /**
     * 商品id
     **/
    @ApiModelProperty(value = "商品id", required = true)
    @NotNull(message = "商品id不能为空")
    private String artProductId;
    /**
     * 购买的商品数量
     **/
    @ApiModelProperty(value = "购买的商品数量", required = true)
    private Integer productAmount;
}
