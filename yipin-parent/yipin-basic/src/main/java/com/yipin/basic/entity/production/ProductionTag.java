package com.yipin.basic.entity.production;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@ApiModel(value = "作品标签表")
@Data
@Entity
@Table(name = "production_tag")
public class ProductionTag {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 分类标签
     */
    private String tagName;
    /**
     * 排序号，越大越在后面
     */
    @ApiModelProperty("排序号，越大越在后面")
    private Integer orderNum;
}
