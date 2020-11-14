package com.yipin.basic.entity.others;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "点赞表")
@Data
@Entity
@Table(name = "likes")
public class Likes implements Serializable {
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
     * 作品id
     */
    private Integer productionId;
    /**
     * 点赞时间
     */
    private Date createTime;
    /**
     * 评论id
     */
    private Integer commentId;
}
