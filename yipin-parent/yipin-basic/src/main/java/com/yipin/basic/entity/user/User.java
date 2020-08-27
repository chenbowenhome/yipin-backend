package com.yipin.basic.entity.user;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "user")
public class User implements Serializable {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 用户openid
     */
    private String openid;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 用户描述
     */
    private String description;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 用户所属单位
     */
    private String unit;
    /**
     * E-Mail
     */
    private String email;
    /**
     * 用户身份
     */
    private Integer identity;
    /**
     * 粉丝数
     */
    private Integer fanscount;
    /**
     * 关注数
     */
    private Integer followcount;
    /**
     * 出生年月日
     */
    private Date birthday;
    /**
     * 注册日期
     */
    private Date regdate;
    /**
     * 最后登陆日期
     */
    private Date lastdate;
    /**
     * 更新时间
     */
    private Date updatetime;
    /**
     * 点赞总数
     */
    private Integer likes;
    /**
     * 礼物总数
     */
    private Integer gifts;
    /**
     * 评估总数
     */
    private Integer assessments;
    /**
     * 代表作
     */
    private Integer mainProductionId;
    /**
     * 个人信息状态，0为未完成，1为已完成
     */
    private Integer informationStatus;
    /**
     * 品值
     */
    private BigDecimal performanceNum;
    /**
     * 本期排名
     */
    private Integer ranking;
}
