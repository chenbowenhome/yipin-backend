package com.yipin.basic.service.impl;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.yipin.basic.VO.ArtActivityVO;
import com.yipin.basic.config.OrderCheckScheduler;
import com.yipin.basic.config.OrderDelayDTO;
import com.yipin.basic.dao.order.OrderDetailRepository;
import com.yipin.basic.dao.order.OrderRepository;
import com.yipin.basic.dao.order.ProductRepository;
import com.yipin.basic.dao.userDao.UserRepository;
import com.yipin.basic.entity.order.ArtOrder;
import com.yipin.basic.entity.order.ArtOrderDetail;
import com.yipin.basic.entity.order.ArtProduct;
import com.yipin.basic.entity.user.User;
import com.yipin.basic.form.OrderForm;
import com.yipin.basic.service.OrderService;
import com.yipin.basic.utils.PayUtils;
import enums.ResultEnum;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.IdWorker;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private OrderCheckScheduler orderCheckScheduler;

    /**选择商品并且下单，默认支付状态为0未支付**/
    @Override
    public Result<JSONObject> wxPay(Integer userId, List<OrderForm> orderFormList, HttpServletRequest request) {
        if (userId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        User user = userRepository.findUserById(userId);
        if (user == null){
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        //此次订单的总金额(单位：分)
        double money = 0.0;
        //生成此次订单的orderId
        String orderId = idWorker.nextId() + "";
        //开始为每个商品生成订单，判断商品库存状态
        List<ArtOrderDetail> orderDetailList = new ArrayList<>();
        for (OrderForm orderForm : orderFormList) {
            ArtProduct artProduct = productRepository.findArtProductById(orderForm.getArtProductId());
            if (artProduct == null){
                return Result.newError("商品不存在");
            }
            double payAmount = 0.0;
            ArtOrderDetail artOrderDetail = new ArtOrderDetail();
            //如果是有库存的商品
            if (artProduct.getProductType() == 0) {
                //判断商品库存是否足够
                if (orderForm.getProductAmount() != null && artProduct.getProductRepertory() < orderForm.getProductAmount()) {
                    return Result.newError("商品库存不足");
                }
                //该商品总金额
                payAmount = artProduct.getProductPrice().doubleValue() * orderForm.getProductAmount();
                //加到订单总金额里面(单位：分)
                money = money + payAmount;
                artOrderDetail.setProductAmount(orderForm.getProductAmount());
            }else if (artProduct.getProductType() == 1){
                //如果是没有库存的商品
                payAmount = artProduct.getProductPrice().doubleValue();
                money = money + payAmount;
            }
            artOrderDetail.setId(idWorker.nextId() + "");
            artOrderDetail.setArtProductId(orderForm.getArtProductId());
            artOrderDetail.setUserId(userId);
            artOrderDetail.setPayAmount(new BigDecimal(payAmount));
            artOrderDetail.setUpdateTime(new Date());
            artOrderDetail.setCreateTime(new Date());
            artOrderDetail.setPayType(artProduct.getPayType());
            artOrderDetail.setOrderId(orderId);
            artOrderDetail.setProductName(artProduct.getProductName());
            artOrderDetail.setProductPrice(artProduct.getProductPrice());
            orderDetailList.add(artOrderDetail);
        }
        //开始生成请求
        int mm = (int) (money * 100);
        String openid = user.getOpenid();//用户openid
        String title = "艺品活动";//商品名称
        String m = mm + "";//商品价格
        System.out.println(m);
        JSONObject json = PayUtils.wxPay(openid,orderId,title,m,request);
        if (json == null){
            return Result.newResult(ResultEnum.PAY_ERROR);
        }
        String msg = (String) json.get("errMsg");
        Map<String, Object> data = (Map<String, Object>)json.get("data");
        if (msg.equals("Failed") || data == null){
            return Result.newResult(ResultEnum.PAY_ERROR);
        }
        String p = (String)data.get("package");
        if (p.equals("prepay_id=null")){
            return Result.newResult(ResultEnum.PAY_ERROR);
        }
        //库存和请求无误，开始创建订单详情
        for (ArtOrderDetail artOrderDetail : orderDetailList) {
            if (artOrderDetail.getProductAmount() != null) {
                //扣库存
                ArtProduct artProduct = productRepository.findArtProductById(artOrderDetail.getArtProductId());
                artProduct.setProductRepertory(artProduct.getProductRepertory() - artOrderDetail.getProductAmount());
                productRepository.save(artProduct);
            }
            //创建订单详情
            orderDetailRepository.save(artOrderDetail);
        }
        //开始创建总订单
        ArtOrder artOrder = new ArtOrder();
        artOrder.setId(orderId);
        artOrder.setUserId(userId);
        artOrder.setMoney(new BigDecimal(money));
        artOrder.setName("艺品官方");
        artOrder.setCreateTime(new Date());
        artOrder.setUpdateTime(new Date());
        artOrder.setOrderStatus(0);
        artOrder.setPayStatus(0);
        orderRepository.save(artOrder);

        //把该订单放入延时队列中，超时订单会被销毁
        OrderDelayDTO order  = new OrderDelayDTO();
        //过期时间设置为2分钟后
        order.setOrderId(orderId);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2);
        order.setExpirationTime(calendar.getTime());
        orderCheckScheduler.put(order);

        json.put("orderId",orderId);
        return Result.newSuccess(json);
    }

    /**支付后调用此接口，改变订单支付状态，可传入的有：1 支付成功、2 支付失败**/
    @Override
    public Result<Void> verifyOrderStatus(String orderId, Integer payStatus) {
        if (orderId == null || payStatus == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        List<ArtOrderDetail> orderDetailList = orderDetailRepository.findArtOrderDetailByOrderId(orderId);
        ArtOrder artOrder = orderRepository.findArtOrderById(orderId);
        if (artOrder == null) {
            //订单不存在的话就返回错误
            return Result.newResult(ResultEnum.ORDER_NOT_EXIT);
        }
        if (payStatus == 2) {
            artOrder.setPayStatus(2);
            artOrder.setOrderStatus(2);
            for (ArtOrderDetail orderDetail : orderDetailList) {
                ArtProduct artProduct = productRepository.findArtProductById(orderDetail.getArtProductId());
                //回库
                if (artProduct != null && artProduct.getProductType() == 0) {
                    artProduct.setProductRepertory(artProduct.getProductRepertory() + orderDetail.getProductAmount());
                    productRepository.save(artProduct);
                }
                orderDetail.setUpdateTime(new Date());
                orderDetailRepository.save(orderDetail);
            }
        }else if(payStatus == 1){
            artOrder.setPayStatus(1);
            artOrder.setOrderStatus(1);
        }
        orderRepository.save(artOrder);
        return Result.newSuccess();
    }

    /**展示商品信息**/
    @Override
    public Result<List<ArtProduct>> listArtProduct(List<String> ids) {
        List<ArtProduct> artProductList = productRepository.findArtProductByIds(ids);
        return Result.newSuccess(artProductList);
    }

    /**根据订单号查询订单信息**/
    @Override
    public Result<List<ArtOrderDetail>> listArtOrderDetailByOrderId(String orderId) {
        if (orderId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        List<ArtOrderDetail> orderDetailList = orderDetailRepository.findArtOrderDetailByOrderId(orderId);
        return Result.newSuccess(orderDetailList);
    }

    /**列出用户所有交易成功的订单**/
    @Override
    public Result<PageVO<ArtOrder>> listOrder(Integer userId,PageArg arg) {
        if (userId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        //查询交易成功的订单
        Page<ArtOrder> orderPage = orderRepository.findArtOrderByUserIdAndPayStatusOrderByCreateTimeDesc(userId,1,pageable);
        //构建pageVo对象
        PageVO<ArtOrder> pageVo = PageVO.<ArtOrder>builder()
                .totalPage(orderPage.getTotalPages())
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(orderPage.getContent())
                .build();
        return Result.newSuccess(pageVo);
    }

    /**查询订单状态**/
    @Override
    public Result<JSONObject> findOrderStatus(String orderId) {
        JSONObject json = PayUtils.verifyOrderStatus(orderId);
        return Result.newSuccess(json);
    }


}
