package com.yipin.basic.entity.master;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "master_production")
public class MasterProduction implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 大师id
     */
    private Integer masterId;
    /**
     * 作品图片
     */
    private String productionImg;
    /**
     * 作品名称
     */
    private String productionName;
    /**
     * 作品描述
     */
    private String productionDescription;
    /**
     * 作品浏览次数
     */
    private Integer productionViews;
}
