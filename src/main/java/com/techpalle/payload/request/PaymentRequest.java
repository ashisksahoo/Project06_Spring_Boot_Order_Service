package com.techpalle.payload.request;

import com.techpalle.utils.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest
{
    private long orderId;
    private long totalAmount;
    private String referenceNumber;
    private PaymentMode paymentMode;
}
