package com.techpalle.service;

import com.techpalle.exception.OrderServiceCustomException;
import com.techpalle.model.Order;
import com.techpalle.payload.request.OrderRequest;
import com.techpalle.payload.request.PaymentRequest;
import com.techpalle.payload.response.OrderResponse;
import com.techpalle.payload.response.PaymentResponse;
import com.techpalle.payload.response.ProductResponse;
import com.techpalle.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
@RequiredArgsConstructor
@ComponentScan
public class OrderServiceImplement implements OrderService
{
    private final OrderRepository orderRepository;
    RestTemplate restTemplate = new RestTemplate();
    @Override
    public long placeOrder(OrderRequest orderRequest)
    {
        log.info("OrderServiceImplement | placeOrder is called");
        log.info("OrderServiceImplement | placeOrder | Placing order request orderRequest : " + orderRequest.toString());
        log.info("OrderServiceImplement | placeOrder | Creating order with status CREATED");
        Order order = Order.builder().
                productId(orderRequest.getProductId()).
                orderStatus("CREATED").
                orderDate(Instant.now()).
                totalAmount(orderRequest.getTotalAmount()).
                quantity(orderRequest.getQuantity()).
                build();
        order = orderRepository.save(order);
        log.info("OrderServiceImplement | placeOrder | Calling payment service to complete the payment");
        PaymentRequest paymentRequest = PaymentRequest.builder().
                orderId(order.getOrderId()).
                paymentMode(orderRequest.getPaymentMode()).
                totalAmount(orderRequest.getTotalAmount()).
                build();
        String orderStatus = null;
        try
        {
            log.info("OrderServiceImplement | placeOrder | Payment done successfully. Changing the order status");
            orderStatus = "PLACED";
        }
        catch (Exception e)
        {
            log.info("OrderServiceImplement | placeOrder | Error occurred in payment. Changing the order status");
            orderStatus = "PAYMENT_FAILED";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        log.info("OrderServiceImplement | placeOrder | Order placed successfully with order id : {}",
                order.getOrderId());
        return order.getOrderId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId)
    {
        log.info("OrderServiceImplement | getOrderDetails | Get order details for order id : {}", orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new OrderServiceCustomException("Order not found for the order id : " + orderId, "NOT_FOUND", 404));
        log.info("OrderServiceImplement | getOrderDetails | Invoking product service to fetch the product for id : {}",
                order.getProductId());
        ProductResponse productResponse = restTemplate.
                getForObject("http://PRODUCT_SERVICE/product/" + order.getProductId(), ProductResponse.class);
        log.info("OrderServiceImplement | getOrderDetails | Getting payment information from the payment service");
        PaymentResponse paymentResponse = restTemplate.
                getForObject("http://PAYMENT_SERVICE/payment/order/" + order.getOrderId(), PaymentResponse.class);
        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder().
                    productName(productResponse.getProductName()).
                    price(productResponse.getPrice()).
                    productId(productResponse.getProductId()).
                    build();
        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder().
                    paymentId(paymentResponse.getPaymentId()).
                    paymentStatus(paymentResponse.getStatus()).
                    paymentDate(paymentResponse.getPaymentDate()).
                    paymentMode(paymentResponse.getPaymentMode()).
                    build();
        OrderResponse orderResponse = OrderResponse.builder().
                orderId(order.getOrderId()).
                orderStatus(order.getOrderStatus()).
                amount(order.getTotalAmount()).
                orderDate(order.getOrderDate()).
                productDetails(productDetails).
                paymentDetails(paymentDetails).
                build();
        log.info("OrderServiceImplement | getOrderDetails | order response : " + orderResponse.toString());
        return orderResponse;
    }
}
