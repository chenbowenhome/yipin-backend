package com.yipin.basic.VO;

import com.yipin.basic.entity.user.UserArt;
import com.yipin.basic.entity.user.UserPerformance;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**与前端进行交互的实体类，因为有些东西不能传到前端**/
@Data
public class UserVO implements Serializable {
    /**
     * ID
     */
    private Integer id;
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
     * 品值
     */
    private BigDecimal performanceNum;
    /**
     * 本期排名
     */
    private Integer ranking;

    private UserArt userArt;

    private UserPerformance userPerformance;

}

