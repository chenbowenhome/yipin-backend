package com.yipin.basic.controller;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.ShoppingCartVO;
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


@Api(tags = "订单相关接口(活动详情页面)")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**选择商品并且下单，默认支付状态为0未支付**/
    @ApiOperation("选择商品集合并且下单,返回调起wx支付的参数和订单id(OrderFormList：artProductId:商品的id;productAmount:购买该商品的数量，如果商品类型为虚拟无库存限制商品的话就不需要传这个参数)")
    @RequestMapping(value = "/wxPay",method = RequestMethod.POST)
    public Result<JSONObject> wxPay(Integer userId, @RequestBody @Valid List<OrderForm> orderFormList, HttpServletRequest request) {
        return orderService.wxPay(userId,orderFormList,request);
    }

    /**支付后调用此接口，改变订单支付状态，可传入的有：1 支付成功、2 支付失败**/
    @ApiOperation("传入订单id和用户支付状态，用户支付后调用此接口，改变订单支付状态，可传入的有：1 支付成功、2 支付失败")
    @RequestMapping(value = "/verifyOrderStatus",method = RequestMethod.POST)
    public Result<Void> verifyOrderStatus(String orderId, Integer payStatus) {
        return orderService.verifyOrderStatus(orderId,payStatus);
    }

    /**展示商品信息**/
    @ApiOperation("展示商品信息(传入商品id的集合，返回商品id对应的商品信息集合)")
    @RequestMapping(value = "/listArtProduct",method = RequestMethod.POST)
    public Result<List<ArtProduct>> listArtProduct(@RequestBody List<String> ids){
        return orderService.listArtProduct(ids);
    }

    /**根据订单号查询订单信息**/
    @ApiOperation("根据订单号查询订单信息(传入订单id，返回该订单购买的所有商品信息)")
    @RequestMapping(value = "/listArtOrderDetailByOrderId",method = RequestMethod.GET)
    public Result<List<ArtOrderDetail>> listArtOrderDetailByOrderId(String orderId){
        return orderService.listArtOrderDetailByOrderId(orderId);
    }

    /**列出用户所有交易成功的订单**/
    @ApiOperation("列出用户所有交易成功的订单(传入用户的id和分页参数，返回订单信息)")
    @RequestMapping(value = "/listOrder",method = RequestMethod.POST)
    public Result<PageVO<ArtOrder>> listOrder(Integer userId, @RequestBody PageArg arg) {
        return orderService.listOrder(userId,arg);
    }

    /**查询订单状态**/
    @ApiOperation("查询订单状态(查询订单在wx那边的的支付状态)")
    @RequestMapping(value = "/findOrderStatus",method = RequestMethod.POST)
    public Result<JSONObject> findOrderStatus(String orderId) {
        return orderService.findOrderStatus(orderId);
    }

    /**将商品加入购物车**/
    @ApiOperation("购物车模块：将商品加入购物车(传入用户id和商品表单集合)")
    @RequestMapping(value = "/putToShoppingCart",method = RequestMethod.POST)
    public Result<Void> putToShoppingCart(Integer userId,@RequestBody @Valid List<OrderForm> orderFormList){
        return orderService.putToShoppingCart(userId,orderFormList);
    }
    /**列出用户购物车内所有商品**/
    @ApiOperation("购物车模块：列出用户购物车内所有商品(传入用户id和分页参数)")
    @RequestMapping(value = "/listShoppingCartProduct",method = RequestMethod.POST)
    public Result<PageVO<ShoppingCartVO>> listShoppingCartProduct(Integer userId,@RequestBody PageArg arg){
        arg.validate();
        return orderService.listShoppingCartProduct(userId,arg);
    }
    /**购买购物车内的商品**/
    @ApiOperation("购物车模块：购买购物车内的商品(即用钱清空购物车，传入购物车id集合)")
    @RequestMapping(value = "/buyShoppingCartProduct",method = RequestMethod.POST)
    public Result<Void> buyShoppingCartProduct(@RequestBody List<Integer> ids){
        return orderService.buyShoppingCartProduct(ids);
    }
    /**移除购物车内的商品**/
    @ApiOperation("购物车模块：移除购物车内的商品(直接删除，不花钱)")
    @RequestMapping(value = "/removeShoppingCartProduct",method = RequestMethod.POST)
    public Result<Void> removeShoppingCartProduct(@RequestBody List<Integer> ids){
        return orderService.removeShoppingCartProduct(ids);
    }
}
