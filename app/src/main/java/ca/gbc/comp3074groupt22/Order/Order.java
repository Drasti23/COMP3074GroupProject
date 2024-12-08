package ca.gbc.comp3074groupt22.Order;

public class Order {
    private String orderNumber;
    private String orderStatus;

    public Order(String orderNumber, String orderStatus) {
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}