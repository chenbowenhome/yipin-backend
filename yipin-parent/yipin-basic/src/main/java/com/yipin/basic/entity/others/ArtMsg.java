package com.yipin.basic.entity.others;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "art_msg")
public class ArtMsg implements Serializable {
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
     * 消息内容
     */
    private String msgDetail;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否被查看
     */
    private Integer viewStatus;
}
