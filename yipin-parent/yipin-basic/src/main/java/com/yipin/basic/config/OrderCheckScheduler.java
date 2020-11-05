package com.yipin.basic.config;

import com.yipin.basic.dao.order.OrderDetailRepository;
import com.yipin.basic.dao.order.OrderRepository;
import com.yipin.basic.dao.order.ProductRepository;
import com.yipin.basic.entity.order.ArtOrder;
import com.yipin.basic.entity.order.ArtOrderDetail;
import com.yipin.basic.entity.order.ArtProduct;
import com.yipin.basic.utils.PayUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;

@Component
public class OrderCheckScheduler implements CommandLineRunner {
    /**延时队列**/
    private DelayQueue<OrderDelayDTO> queue = new DelayQueue<>();

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    /**
     * 加入到延时队列中
     */
    public void put(OrderDelayDTO task) {
        System.out.println("加入延时任务");
        queue.put(task);
    }

    /**
     * 执行线程
     **/
    public void executeThread() {
        while (true) {
            try {
                OrderDelayDTO take = queue.take();
                String orderId = take.getOrderId();
                if (orderId != null) {
                    List<ArtOrderDetail> orderDetailList = orderDetailRepository.findArtOrderDetailByOrderId(orderId);
                    ArtOrder artOrder = orderRepository.findArtOrderById(orderId);
                    //如果订单的状态还是新创建的就销毁订单
                    if (artOrder.getOrderStatus() == 0) {
                        JSONObject json = PayUtils.verifyOrderStatus(orderId);
                        String trade_state = (String) json.get("trade_state");
                        if (trade_state == "SUCCESS"){
                            //如果订单支付成功，则改变订单状态
                            artOrder.setPayStatus(1);
                            artOrder.setOrderStatus(1);
                            orderRepository.save(artOrder);
                        }else if(trade_state == "USERPAYING"){
                            //如果用户正在支付，则重新加入队列
                            OrderDelayDTO order  = new OrderDelayDTO();
                            //过期时间设置为1分钟后
                            order.setOrderId(orderId);
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.MINUTE, 1);
                            order.setExpirationTime(calendar.getTime());
                            put(order);
                        }else {
                            //销毁订单
                            artOrder.setOrderStatus(2);
                            for (ArtOrderDetail orderDetail : orderDetailList) {
                                ArtProduct artProduct = productRepository.findArtProductById(orderDetail.getArtProductId());
                                //商品回库
                                if (artProduct != null && artProduct.getProductType() == 0) {
                                    artProduct.setProductRepertory(artProduct.getProductRepertory() + orderDetail.getProductAmount());
                                    productRepository.save(artProduct);
                                }
                                orderDetail.setUpdateTime(new Date());
                                orderDetailRepository.save(orderDetail);
                            }
                            orderRepository.save(artOrder);
                        }
                    }
                }
                System.out.println("订单编号：" + take.getOrderId() + " 过期时间：" + take.getExpirationTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化,spring启动后执行
     **/
    @Override
    public void run(String... args) throws Exception {
        System.out.println("初始化延时队列");
        Executors.newSingleThreadExecutor().execute(new Thread(this::executeThread));
    }
}
