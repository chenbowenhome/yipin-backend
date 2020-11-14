package com.yipin.basic.entity.master;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@ApiModel(value = "大师表")
@Data
@Entity
@Table(name = "master")
public class Master implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 大师名字
     */
    @ApiModelProperty("大师名字")
    private String masterName;
    /**
     * 大师头像
     */
    @ApiModelProperty("大师头像")
    private String masterAvatar;
    /**
     * 大师背景图片
     */
    @ApiModelProperty("大师背景图片")
    private String backgroundImg;
    /**
     * 大师描述
     */
    @ApiModelProperty("大师描述")
    private String masterDescription;

}
