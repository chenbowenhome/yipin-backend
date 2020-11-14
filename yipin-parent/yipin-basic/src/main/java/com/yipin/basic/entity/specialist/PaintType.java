package com.yipin.basic.entity.specialist;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@ApiModel(value = "画种表")
@Data
@Entity
@Table(name = "paint_type")
public class PaintType implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 画种名称
     */
    @ApiModelProperty("画种名称")
    private String typeName;
}
