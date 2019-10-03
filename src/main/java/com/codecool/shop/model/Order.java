package com.codecool.shop.model;


public class Order {
    private int id;
    private User user;
    private Cart cart;
    private UserAddress address;
    private String status;

    public Order(User user, Cart cart, UserAddress address, String status) {
        this.user = user;
        this.cart = cart;
        this.address = address;
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public UserAddress getAddress() {return address;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", cart=" + cart +
                ", address=" + address +
                ", status='" + status + '\'' +
                '}';
    }
}
