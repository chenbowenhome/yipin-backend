package com.yipin.basic.service;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.entity.order.ArtOrder;
import com.yipin.basic.entity.order.ArtOrderDetail;
import com.yipin.basic.entity.order.ArtProduct;
import com.yipin.basic.form.OrderForm;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {
    /**选择商品并且下单，默认支付状态为0未支付**/
    Result<JSONObject> wxPay(Integer userId, List<OrderForm> orderFormList, HttpServletRequest request);
    /**支付后调用此接口，改变订单支付状态，可传入的有：1 支付成功、2 支付失败**/
    Result<Void> verifyOrderStatus(String orderId,Integer payStatus);
    /**展示商品信息**/
    Result<List<ArtProduct>> listArtProduct(List<String> ids);
    /**根据订单号查询订单信息**/
    Result<List<ArtOrderDetail>> listArtOrderDetailByOrderId(String orderId);
    /**列出用户所有交易成功的订单**/
    Result<PageVO<ArtOrder>> listOrder(Integer userId,PageArg arg);
    /**查询订单状态**/
    Result<JSONObject> findOrderStatus(String orderId);
}
