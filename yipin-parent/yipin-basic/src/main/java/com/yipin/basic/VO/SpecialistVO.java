package com.yipin.basic.VO;

import lombok.Data;
import java.io.Serializable;

@Data
public class SpecialistVO implements Serializable {
    /**
     * 专家id
     */
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 头像
     */
    private String avatar;
}
