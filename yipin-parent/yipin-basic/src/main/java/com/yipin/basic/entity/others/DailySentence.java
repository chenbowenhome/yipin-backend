package com.yipin.basic.entity.others;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "daily_sentence")
public class DailySentence implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 句子
     */
    private String content;
    /**
     * 图片
     */
    private String imgUrl;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 句子状态
     */
    private Integer nowStatus;
}
