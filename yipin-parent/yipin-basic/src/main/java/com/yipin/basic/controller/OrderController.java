package com.yipin.basic.controller;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.entity.order.ArtOrder;
import com.yipin.basic.entity.order.ArtOrderDetail;
import com.yipin.basic.entity.order.ArtProduct;
import com.yipin.basic.form.OrderForm;
import com.yipin.basic.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@Api(tags = "订单相关接口")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**选择商品并且下单，默认支付状态为0未支付**/
    @ApiOperation("选择商品并且下单，默认支付状态为0未支付")
    @RequestMapping(value = "/wxPay",method = RequestMethod.POST)
    public Result<JSONObject> wxPay(Integer userId, @RequestBody @Valid List<OrderForm> orderFormList, HttpServletRequest request) {
        return orderService.wxPay(userId,orderFormList,request);
    }

    /**支付后调用此接口，改变订单支付状态，可传入的有：1 支付成功、2 支付失败**/
    @ApiOperation("支付后调用此接口，改变订单支付状态，可传入的有：1 支付成功、2 支付失败")
    @RequestMapping(value = "/verifyOrderStatus",method = RequestMethod.POST)
    public Result<Void> verifyOrderStatus(String orderId, Integer payStatus) {
        return orderService.verifyOrderStatus(orderId,payStatus);
    }

    /**展示商品信息**/
    @ApiOperation("展示商品信息")
    @RequestMapping(value = "/listArtProduct",method = RequestMethod.POST)
    public Result<List<ArtProduct>> listArtProduct(@RequestBody List<String> ids){
        return orderService.listArtProduct(ids);
    }

    /**根据订单号查询订单信息**/
    @ApiOperation("根据订单号查询订单信息")
    @RequestMapping(value = "/listArtOrderDetailByOrderId",method = RequestMethod.GET)
    public Result<List<ArtOrderDetail>> listArtOrderDetailByOrderId(String orderId){
        return orderService.listArtOrderDetailByOrderId(orderId);
    }

    /**列出用户所有交易成功的订单**/
    @ApiOperation("列出用户所有交易成功的订单")
    @RequestMapping(value = "/listOrder",method = RequestMethod.POST)
    public Result<PageVO<ArtOrder>> listOrder(Integer userId, @RequestBody PageArg arg) {
        return orderService.listOrder(userId,arg);
    }

    /**查询订单状态**/
    @ApiOperation("查询订单状态")
    @RequestMapping(value = "/findOrderStatus",method = RequestMethod.POST)
    public Result<JSONObject> findOrderStatus(String orderId) {
        return orderService.findOrderStatus(orderId);
    }
}
