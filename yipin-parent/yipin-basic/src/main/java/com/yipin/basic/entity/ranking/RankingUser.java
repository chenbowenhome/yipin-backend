package com.yipin.basic.entity.ranking;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "ranking_user")
public class RankingUser implements Serializable {
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
     * 周期数
     */
    private Integer period;
    /**
     * 用户排名
     */
    private Integer ranking;
    /**
     * 创建时间
     */
    private Date createTime;
}
