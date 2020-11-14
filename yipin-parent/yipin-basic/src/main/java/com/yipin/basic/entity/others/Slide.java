package com.yipin.basic.entity.others;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@ApiModel(value = "轮播图表")
@Data
@Entity
@Table(name = "slide")
public class Slide implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 作品url
     */
    private String url;
    /**
     * 排序，越大的越在前面
     */
    @ApiModelProperty("排序，越大的越在前面")
    private Integer orderNum;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
}
