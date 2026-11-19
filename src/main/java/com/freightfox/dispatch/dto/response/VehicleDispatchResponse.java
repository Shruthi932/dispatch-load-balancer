
package com.freightfox.dispatch.dto.response;

import java.util.List;

public class VehicleDispatchResponse {

    private String vehicleId;
    private double capacity;
    private double totalLoad;
    private String totalDistance;
    private List<AssignedOrderResponse> assignedOrders;

    public VehicleDispatchResponse() {
    }

    public VehicleDispatchResponse(
            String vehicleId,
            double capacity,
            double totalLoad,
            String totalDistance,
            List<AssignedOrderResponse> assignedOrders) {

        this.vehicleId = vehicleId;
        this.capacity = capacity;
        this.totalLoad = totalLoad;
        this.totalDistance = totalDistance;
        this.assignedOrders = assignedOrders;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public double getCapacity() {
        return capacity;
    }

    public double getTotalLoad() {
        return totalLoad;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public List<AssignedOrderResponse> getAssignedOrders() {
        return assignedOrders;
    }
}
