package com.techpalle.controller;

import com.techpalle.payload.request.OrderRequest;
import com.techpalle.payload.response.OrderResponse;
import com.techpalle.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Log4j2
public class OrderController
{
    private final OrderService orderService;
    @PostMapping("/palceOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest)
    {
        log.info("OrderController | placeOrder is called");
        log.info("OrderController | placeOrder | orderRequest : " + orderRequest.toString());
        long orderId = orderService.placeOrder(orderRequest);
        log.info("Order id : {}", orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable long orderId)
    {
        log.info("OrderController | getOrderDetails is called");
        OrderResponse orderResponse = orderService.getOrderDetails(orderId);
        log.info("OrderController | getOrderDetails | orderResponse : " + orderResponse.toString());
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }
}
