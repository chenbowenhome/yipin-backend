package com.yipin.basic.entity.others;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "消息表")
@Data
@Entity
@Table(name = "art_msg")
public class ArtMsg implements Serializable {
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
     * 接收信息的用户id
     */
    @ApiModelProperty("接收信息的用户id")
    private Integer receiveUserId;
    /**
     * 消息内容
     */
    @ApiModelProperty("消息内容")
    private String msgDetail;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否被查看
     */
    @ApiModelProperty("是否被查看")
    private Integer viewStatus;
}
