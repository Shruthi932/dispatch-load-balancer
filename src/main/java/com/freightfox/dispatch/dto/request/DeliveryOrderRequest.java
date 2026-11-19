
package com.freightfox.dispatch.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import com.freightfox.dispatch.enums.OrderPriority;
import jakarta.validation.constraints.*;

public class DeliveryOrderRequest {

    @NotBlank(message = "Order ID is required")
    @Schema(example = "ORD001")
    private String orderId;

    @NotNull
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    @Schema(example = "12.9716")
    private Double latitude;

    @NotNull
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    @Schema(example = "77.6413")
    private Double longitude;

    @NotBlank(message = "Address is required")
    @Schema(example = "MG Road, Bangalore, Karnataka, India")
    private String address;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Schema(example = "10.0")
    private Double packageWeight;

    @NotNull(message = "Order priority is required")
    @Schema(example = "HIGH")
    private OrderPriority priority;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(Double packageWeight) {
        this.packageWeight = packageWeight;
    }

    public OrderPriority getPriority() {
        return priority;
    }

    public void setPriority(OrderPriority priority) {
        this.priority = priority;
    }
}
