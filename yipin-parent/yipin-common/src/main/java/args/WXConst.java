package args;

public class WXConst {
    //微信小程序appid
    public static String appId = "wx8ea7478b63d61736";
    //微信小程序appsecret
    public static String appSecret = "069f9a3e6b3fb93f975d78b173da9302";
    //微信商户号
    public static String mch_id="1603353979";
    //微信支付的商户密钥
    public static final String key = "9j5ybb6myw3xvnggiixtj9kc5g0dn9ba";
    //获取微信Openid的请求地址
    public static String WxGetOpenIdUrl = "https://api.weixin.qq.com/sns/jscode2session";
    //支付成功后的服务器回调url
    public static final String notify_url="https://api.weixin.qq.com/sns/jscode2session";
    //查询订单url
    public static final String verify_url = "https://api.mch.weixin.qq.com/pay/orderquery";
    //签名方式
    public static final String SIGNTYPE = "MD5";
    //交易类型
    public static final String TRADETYPE = "JSAPI";
    //微信统一下单接口地址
    public static final String pay_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
}
