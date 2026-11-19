
package com.freightfox.dispatch.dto.response;

import com.freightfox.dispatch.enums.OrderPriority;

public class AssignedOrderResponse {

    private String orderId;
    private String address;
    private double packageWeight;
    private OrderPriority priority;

    public AssignedOrderResponse() {
    }

    public AssignedOrderResponse(
            String orderId,
            String address,
            double packageWeight,
            OrderPriority priority) {

        this.orderId = orderId;
        this.address = address;
        this.packageWeight = packageWeight;
        this.priority = priority;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getAddress() {
        return address;
    }

    public double getPackageWeight() {
        return packageWeight;
    }

    public OrderPriority getPriority() {
        return priority;
    }
}
