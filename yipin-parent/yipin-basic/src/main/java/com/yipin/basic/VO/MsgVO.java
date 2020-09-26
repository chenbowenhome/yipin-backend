package com.yipin.basic.VO;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class MsgVO implements Serializable {
    /**
     * 头像
     */
    private String avatar;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * ID
     */
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
