package com.nishikant.inventoryService.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ORDERSERVICE", path = "/orders")
public interface OrdersFeignClient {

    @GetMapping("/core/helloOrders")
    String helloOrders();

}
