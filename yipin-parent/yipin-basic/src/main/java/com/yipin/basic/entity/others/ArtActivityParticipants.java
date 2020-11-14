package com.yipin.basic.entity.others;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "活动报名人员表")
@Data
@Entity
@Table(name = "art_activity_participants")
public class ArtActivityParticipants implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Integer userId;
    /**
     * 活动id
     */
    @ApiModelProperty("活动id")
    private Integer artActivityId;
    /**
     * 参加时间
     */
    @ApiModelProperty("参加时间")
    private Date createTime;
}
