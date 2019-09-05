package com.codecool.shop.dao.implementation;


public class OrderMem {


    private static OrderMem instance = null;
    public String name;
    public String email;
    public String phoneNum;
    public String billingCountry;
    public String billingCity;
    public String billingZipCode;
    public String billingAddress;
    public String shippingCountry;
    public String shippingCity;
    public String shippingZipCode;
    public String shippingAddress;

    /* A private Constructor prevents any other class from instantiating.
     */
    private OrderMem() {
    }

    public static OrderMem getInstance() {
        if (instance == null) {
            instance = new OrderMem();
        }
        return instance;
    }

    public void setOrderInfo(String name, String email, String phoneNum, String billingCountry, String billingCity,
                             String billingZipCode, String billingAddress, String shippingCountry, String shippingCity,
                             String shippingZipCode, String shippingAddress) {
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
        this.billingCountry = billingCountry;
        this.billingCity = billingCity;
        this.billingZipCode = billingZipCode;
        this.billingAddress = billingAddress;
        this.shippingCountry = shippingCountry;
        this.shippingCity = shippingCity;
        this.shippingZipCode = shippingZipCode;
        this.shippingAddress = shippingAddress;
    }

    public String[] getOrderInfo() {
        return new String[]{name, email, phoneNum, billingCountry, billingCity, billingZipCode, billingAddress,
                shippingCountry, shippingCity, shippingZipCode, shippingAddress};
    }
}
