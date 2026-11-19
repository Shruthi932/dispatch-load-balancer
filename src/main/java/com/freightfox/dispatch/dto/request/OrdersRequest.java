
package com.freightfox.dispatch.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class OrdersRequest {

    @NotEmpty(message = "At least one order is required")
    @Valid
    private List<DeliveryOrderRequest> orders;

    public List<DeliveryOrderRequest> getOrders() {
        return orders;
    }

    public void setOrders(List<DeliveryOrderRequest> orders) {
        this.orders = orders;
    }
}
