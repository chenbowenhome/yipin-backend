package com.yipin.basic.entity.user;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "follow")
public class Follow implements Serializable {
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
     * 被关注的用户id
     */
    private Integer followUserId;
    /**
     * 创建时间
     */
    private Date createTime;
}
