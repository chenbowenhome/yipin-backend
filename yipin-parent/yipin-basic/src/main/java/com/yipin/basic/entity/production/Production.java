package com.yipin.basic.entity.production;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "production")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Production implements Serializable {
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
     * 作品标题
     */
    private String title;
    /**
     * 作品描述
     */
    private String description;
    /**
     * 图片url
     */
    @Type( type = "json" )
    @Column( columnDefinition = "json" )
    private List<String> imgUrl;
    /**
     * 审核状态
     */
    private Integer checkStatus;
    /**
     * 发布状态
     */
    private Integer publishStatus;
    /**
     * 评估状态
     */
    private Integer evaluateStatus;
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
     * 是否为代表作
     */
    private Integer isMainProduction;
    /**
     * 分类标签id
     */
    private Integer tagId;
    /**
     * 删除状态
     */
    private Integer deleteStatus;
    /**
     * 收藏数量
     */
    private Integer collectNums;
}
