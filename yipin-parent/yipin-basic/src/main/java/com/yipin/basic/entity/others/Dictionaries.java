package com.yipin.basic.entity.others;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "dictionaries")
public class Dictionaries implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 名字
     */
    private String name;
    /**
     * key
     */
    private String key;
    /**
     * value
     */
    private String value;
    /**
     * 描述
     */
    private String description;
    /**
     * 更新时间
     */
    private Date updateTime;
}
