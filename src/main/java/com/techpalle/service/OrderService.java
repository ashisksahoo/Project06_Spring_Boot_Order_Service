package com.techpalle.service;

import com.techpalle.payload.request.OrderRequest;
import com.techpalle.payload.response.OrderResponse;

public interface OrderService
{
    long placeOrder(OrderRequest orderRequest);
    OrderResponse getOrderDetails(long orderId);
}
