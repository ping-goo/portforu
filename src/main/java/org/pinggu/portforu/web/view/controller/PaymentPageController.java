package org.pinggu.portforu.web.view.controller;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.domain.member.entity.Member;
import org.pinggu.portforu.domain.subscribe.entity.Subscribe;
import org.pinggu.portforu.domain.subscribe.service.SubscribeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pay")
public class PaymentPageController {

    private final SubscribeService subscribeService;

    @Value("${toss.test-client-key}")
    private String clientKey;

    @Value("${toss.success-url}")
    private String successUrl;

    @Value("${toss.fail-url}")
    private String failUrl;

    @Value("${toss.order-id-prefix}")
    private String orderIdPrefix;

    @GetMapping
    public String paymentPage(@RequestParam Long subscribeId, Model model) {
        Subscribe subscribe = subscribeService.findById(subscribeId);
        Member member = subscribe.getMember();

        String orderId = orderIdPrefix + "-" + subscribeId + "-" + System.currentTimeMillis();

        model.addAttribute("clientKey", clientKey);
        model.addAttribute("orderId", orderId);
        model.addAttribute("amount", subscribe.getMembership().getPrice());
        model.addAttribute("orderName", subscribe.getMembership().getName());
        model.addAttribute("customerName", member.getName());
        model.addAttribute("successUrl", successUrl);
        model.addAttribute("failUrl", failUrl);

        return "payment";
    }
}

