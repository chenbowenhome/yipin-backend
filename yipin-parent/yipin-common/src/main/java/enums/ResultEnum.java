package enums;

import lombok.Getter;
import lombok.ToString;

/**
 * 统一状态码响应
 */
@ToString
public enum ResultEnum {
    SYSTEM_ERROR(-1, "系统错误,请联系管理员"),
    SUCCESS(1, "成功"),
    LOGIN_ERROR(2,"用户名或密码错误"),
    PARAM_ERROR(3, "输入参数错误,请校验参数重新输入"),
    SQL_UNIQUE_ERROR(4, "数据库已有重复值"),
    AUTHENTICATION_ERROR(5, "您还未登录"),
    USER_NOT_EXIST(6, "用户不存在"),
    NO_GOODS_MSG(7,"作品不存在"),
    ROLE_ERROR(8, "您不具备该角色权限"),
    NO_LABEL_MSG(9,"标签不存在"),
    NO_ARTICLE_MSG(10,"文章不存在"),
    REGISTER_ERROR(11,"验证码错误"),
    REGISTER_MOBILE_ERROR(12,"注册失败，电话号码已经被注册"),
    FRIEND_ALREADY_EXIST(13,"好友已添加，无需重复添加"),
    REMOTE_ERROR(14, "远程调用失败"),
    PRODUCTION_ERROR(15,"该作品不能设置为代表作，可能原因：未审核、未评估、未发布"),
    PAY_ERROR(16,"支付失败"),
    ORDER_NOT_EXIT(17,"订单不存在"),
    ;

    @Getter
    private Integer code;
    @Getter
    private String msg;

    ResultEnum(Integer code, String errMsg) {
        this.code = code;
        this.msg = errMsg;
    }
}
