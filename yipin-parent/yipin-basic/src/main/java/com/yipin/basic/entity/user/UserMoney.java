package com.yipin.basic.entity.user;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "user_money")
public class UserMoney implements Serializable {
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
     * 用户未币
     */
    private BigDecimal money;
    /**
     * 更新时间
     */
    private Date updateTime;
}
