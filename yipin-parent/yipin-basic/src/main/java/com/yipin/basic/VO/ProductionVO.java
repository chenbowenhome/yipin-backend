package com.yipin.basic.VO;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ProductionVO implements Serializable {
    /**
     * ID
     */
    private Integer id;
    /**
     * 作品标题
     */
    private String title;
    /**
     * 作品描述
     */
    private String description;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 发布状态
     */
    private Integer publishStatus;
    /**
     * 评估状态
     */
    private Integer evaluateStatus;
    /**
     * 图片url
     */
    private List<String> imgUrl;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 浏览数
     */
    private Integer views;
    /**
     * 点赞数
     */
    private Integer likes;
    /**
     * 评论数
     */
    private Integer comments;
    /**
     * 用户信息
     */
    private UserVO userVO;
}
