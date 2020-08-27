package com.yipin.basic.entity.production;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "production_assessment")
public class ProductionAssessment implements Serializable {

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
     * 色调
     */
    private BigDecimal hue;
    /**
     * 空间
     */
    private BigDecimal space;
    /**
     * 造型
     */
    private BigDecimal sculpt;
    /**
     * 构图
     */
    private BigDecimal composition;
    /**
     * 视觉肌理
     */
    private BigDecimal visual;
    /**
     *画面创新
     */
    private BigDecimal creative;
    /**
     * 作品匹配度
     */
    private BigDecimal matchingRate;
    /**
     * 作品潜力值
     */
    private BigDecimal potentialValue;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}