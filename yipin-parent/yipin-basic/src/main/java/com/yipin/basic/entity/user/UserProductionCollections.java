package com.yipin.basic.entity.user;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "user_production_collections")
public class UserProductionCollections implements Serializable {
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
     * 收藏作品id
     */
    private Integer productionId;
    /**
     * 收藏时间
     */
    private Date createTime;
}
