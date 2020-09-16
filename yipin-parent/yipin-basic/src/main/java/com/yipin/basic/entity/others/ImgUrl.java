package com.yipin.basic.entity.others;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "img_url")
public class ImgUrl implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 图片地址
     */
    private String url;
    /**
     * 图片相对地址
     */
    private String relativeUrl;
    /**
     * 图片大小
     */
    private Long imgSize;
}
