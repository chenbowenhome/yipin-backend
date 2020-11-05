package com.yipin.basic.entity.master;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "master")
public class Master implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 大师名字
     */
    private String masterName;
    /**
     * 大师头像
     */
    private String masterAvatar;
    /**
     * 大师背景图片
     */
    private String backgroundImg;
    /**
     * 大师描述
     */
    private String masterDescription;

}
