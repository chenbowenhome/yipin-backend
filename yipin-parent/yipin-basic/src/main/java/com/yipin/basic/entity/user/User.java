package com.yipin.basic.entity.user;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel(value = "用户表")
@Data
@Entity
@Table(name = "user")
@TypeDef(name = "json", typeClass = JsonStringType.class)
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
    @ApiModelProperty("用户openid")
    private String openid;
    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;
    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;
    /**
     * 用户描述
     */
    @ApiModelProperty("用户描述")
    private String description;
    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String mobile;
    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private Integer sex;
    /**
     * 用户所属单位
     */
    @ApiModelProperty("用户所属单位")
    private String unit;
    /**
     * E-Mail
     */
    @ApiModelProperty("E-Mail")
    private String email;
    /**
     * 用户身份
     */
    @ApiModelProperty("用户身份")
    private Integer identity;
    /**
     * 粉丝数
     */
    @ApiModelProperty("粉丝数")
    private Integer fanscount;
    /**
     * 关注数
     */
    @ApiModelProperty("关注数")
    private Integer followcount;
    /**
     * 出生年月日
     */
    @ApiModelProperty("出生年月日")
    private Date birthday;
    /**
     * 注册日期
     */
    @ApiModelProperty("注册日期")
    private Date regdate;
    /**
     * 最后登陆日期
     */
    @ApiModelProperty("最后登陆日期")
    private Date lastdate;
    /**
     * 更新时间
     */
    private Date updatetime;
    /**
     * 点赞总数
     */
    @ApiModelProperty("点赞总数")
    private Integer likes;
    /**
     * 礼物总数
     */
    @ApiModelProperty("礼物总数")
    private Integer gifts;
    /**
     * 评估总数
     */
    @ApiModelProperty("评估总数")
    private Integer assessments;
    /**
     * 代表作
     */
    @ApiModelProperty("代表作")
    @Type( type = "json" )
    @Column( columnDefinition = "json" )
    private List<Integer> mainProductionId;
    /**
     * 个人信息状态，0为未完成，1为已完成
     */
    @ApiModelProperty("个人信息状态，0为未完成，1为已完成")
    private Integer informationStatus;
    /**
     * 品值
     */
    @ApiModelProperty("品值")
    private BigDecimal performanceNum;
    /**
     * 本期排名
     */
    @ApiModelProperty("本期排名")
    private Integer ranking;
    /**
     * 总作品数
     */
    @ApiModelProperty("总作品数")
    private Integer productionNum;
    /**
     * 是否为专家
     */
    @ApiModelProperty("是否为专家")
    private Integer isSpecialist;
}
