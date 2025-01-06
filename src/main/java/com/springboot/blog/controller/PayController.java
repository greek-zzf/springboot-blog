package com.springboot.blog.controller;

import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.*;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Random;

/**
 * 支付相关
 *
 * @version 1.0 2017/3/2
 * @auther <a href="mailto:lly835@163.com">廖师兄</a>
 * @since 1.0
 */
@Controller
@RequestMapping("/app")
@Slf4j
public class PayController {


    @Autowired
    private BestPayServiceImpl bestPayService;

    /**
     * 发起支付
     */
    @PostMapping(value = "/pay")
    @ResponseBody
    public PayResponse pay(@RequestParam String orderId,
                           @RequestParam Double amount) {
        //支付请求参数
        PayRequest request = new PayRequest();
        request.setPayTypeEnum(BestPayTypeEnum.ALIPAY_PC);
        request.setOrderId(orderId);
        request.setOrderAmount(amount);
        request.setOrderName("最好的支付sdk");
        request.setAttach("这里是附加的信息");

        log.info("【发起支付】request={}", JsonUtil.toJson(request));
        PayResponse payResponse = bestPayService.pay(request);
        log.info("【发起支付】response={}", JsonUtil.toJson(payResponse));
        return payResponse;
    }


    @GetMapping("/query")
    @ResponseBody
    public OrderQueryResponse query(@RequestParam String orderId,
                                    @RequestParam("platform") BestPayPlatformEnum platformEnum) {
        OrderQueryRequest orderQueryRequest = new OrderQueryRequest();
        orderQueryRequest.setOrderId(orderId);
        orderQueryRequest.setPlatformEnum(platformEnum);
        OrderQueryResponse queryResponse = bestPayService.query(orderQueryRequest);
        return queryResponse;
    }

    /**
     * 发起支付
     */
    @GetMapping(value = "/alipay/pay")
    public ModelAndView aliPay(Map<String, Object> map) {
        PayRequest request = new PayRequest();
        Random random = new Random();
        //支付请求参数
        request.setPayTypeEnum(BestPayTypeEnum.ALIPAY_PC);
        request.setOrderId(String.valueOf(random.nextInt(1000000000)));
        request.setOrderAmount(0.01);
        request.setOrderName("最好的支付sdk");
        log.info("【发起支付】request={}", JsonUtil.toJson(request));
        PayResponse payResponse = bestPayService.pay(request);
        log.info("【发起支付】response={}", JsonUtil.toJson(payResponse));
        map.put("payResponse", payResponse);

        return new ModelAndView("pay/alipayPc", map);
    }

    /**
     * 异步回调
     */
    @PostMapping(value = "/notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        log.info("【异步通知】支付平台的数据request={}", notifyData);
        PayResponse response = bestPayService.asyncNotify(notifyData);
        log.info("【异步通知】处理后的数据data={}", JsonUtil.toJson(response));

        //返回成功信息给支付平台，否则会不停的异步通知
        if (response.getPayPlatformEnum() == BestPayPlatformEnum.WX) {
            return new ModelAndView("pay/responeSuccessForWx");
        } else if (response.getPayPlatformEnum() == BestPayPlatformEnum.ALIPAY) {
            return new ModelAndView("pay/responeSuccessForAlipay");
        }
        throw new RuntimeException("错误的支付平台");
    }

    @GetMapping("/pay/close")
    @ResponseBody
    public CloseResponse close(@RequestParam String orderId) {
        CloseRequest request = new CloseRequest();
        request.setPayTypeEnum(BestPayTypeEnum.ALIPAY_PC);
        request.setOrderId(orderId);

        CloseResponse close = bestPayService.close(request);
        return close;
    }

    @PostMapping("/nodeMonitor")
    @ResponseBody
    public void report(@RequestBody String body) {
        System.out.println(body);
    }
}
