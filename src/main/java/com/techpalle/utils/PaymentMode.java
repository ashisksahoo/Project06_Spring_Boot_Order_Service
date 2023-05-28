package com.techpalle.utils;

public enum PaymentMode
{
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    DEFAULT("Cash"),
    NET_BANKING("Net Banking");
    private final String displayName;

    PaymentMode(String displayName)
    {
        this.displayName = displayName;
    }

    public String getName()
    {
        return displayName;
    }
}
