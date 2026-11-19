
package com.freightfox.dispatch.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class VehiclesRequest {

    @NotEmpty(message = "At least one vehicle is required")
    @Valid
    private List<VehicleRequest> vehicles;

    public List<VehicleRequest> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleRequest> vehicles) {
        this.vehicles = vehicles;
    }
}
