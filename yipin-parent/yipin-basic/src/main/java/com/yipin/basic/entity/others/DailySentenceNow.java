package com.yipin.basic.entity.others;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "历史与现在每日一句表")
@Data
@Entity
@Table(name = "daily_sentence_now")
public class DailySentenceNow implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 句子id
     */
    @ApiModelProperty("句子id")
    private Integer dailySentenceId;
    /**
     * 句子时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("句子时间")
    private Date nowDate;
}
