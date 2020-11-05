package com.yipin.basic.VO;

import lombok.Data;
import java.io.Serializable;

@Data
public class MasterAvatarVO implements Serializable {
    /**
     * ID
     */
    private Integer id;
    /**
     * 大师头像
     */
    private String masterAvatar;
}
