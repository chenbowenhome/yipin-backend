package com.yipin.basic.entity.others;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "art_activity_participants")
public class ArtActivityParticipants implements Serializable {
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
     * 活动id
     */
    private Integer artActivityId;
    /**
     * 参加时间
     */
    private Date createTime;
}
