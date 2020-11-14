package com.yipin.basic.entity.others;

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

@ApiModel(value = "活动表")
@Data
@Entity
@Table(name = "art_activity")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ArtActivity implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 活动内容
     */
    @ApiModelProperty("活动内容")
    private String content;
    /**
     * 活动图片
     */
    @ApiModelProperty("活动图片")
    private String topicImage;
    /**
     * 活动标题
     */
    @ApiModelProperty("活动标题")
    private String title;
    /**
     * 评论数
     */
    @ApiModelProperty("评论数")
    private Integer comments;
    /**
     * 浏览次数
     */
    @ApiModelProperty("浏览次数")
    private Integer views;
    /**
     * 点赞数
     */
    @ApiModelProperty("点赞数")
    private Integer likes;
    /**
     * 活动开始时间
     */
    @ApiModelProperty("活动开始时间")
    private Date startTime;
    /**
     * 活动结束时间
     */
    @ApiModelProperty("活动结束时间")
    private Date endTime;
    /**
     * 活动参加人数
     */
    @ApiModelProperty("活动参加人数")
    private Integer participants;
    /**
     * 活动费用
     */
    @ApiModelProperty("活动费用")
    private BigDecimal cost;
    /**
     * 是否结束，0为未结束，1为已结束，默认为0
     */
    @ApiModelProperty("是否结束，0为未结束，1为已结束，默认为0")
    private Integer isEnd;

    private Date createTime;
    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private String activityProductId;
    /**
     * 活动附属商品ids
     */
    @ApiModelProperty("活动附属商品ids")
    @Type( type = "json" )
    @Column( columnDefinition = "json" )
    private List<String> othersProductIds;
    /**
     * 是否为轮播图显示的活动，0为不是，1为是
     */
    @ApiModelProperty("是否为轮播图显示的活动，0为不是，1为是")
    private Integer slideStatus;

}
