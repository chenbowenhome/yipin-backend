package com.yipin.basic.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@ApiModel(value = "用户艺能表")
@Data
@Entity
@Table(name = "user_art")
public class UserArt {

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
     * 樱桃个数
     */
    @ApiModelProperty("樱桃个数")
    private Integer cherry;
    /**
     * 草莓个数
     */
    @ApiModelProperty("草莓个数")
    private Integer strawberry;
    /**
     * 苹果个数
     */
    @ApiModelProperty("苹果个数")
    private Integer apple;
    /**
     * 当前在线小时数
     */
    @ApiModelProperty("当前在线小时数")
    private Integer onlineMinute;
    /**
     * 总在线小时数
     */
    @ApiModelProperty("总在线小时数")
    private  Integer allOnlineHours;
    /**
     * 升级所需小时数
     */
    @ApiModelProperty("升级所需小时数")
    private Integer upgradeHours;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    private Date startTime;

    private Date endTime;

}
