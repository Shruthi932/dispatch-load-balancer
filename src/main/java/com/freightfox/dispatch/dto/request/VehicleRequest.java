
package com.freightfox.dispatch.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public class VehicleRequest {
    @Schema(example = "VEH001")
    @NotBlank(message = "Vehicle ID is required")
    private String vehicleId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Schema(example = "100.0")
    private Double capacity;

    @NotNull
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    @Schema(example = "12.9716")
    private Double currentLatitude;

    @NotNull
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    @Schema(example = "77.6413")
    private Double currentLongitude;


    @NotBlank(message = "Current address is required")
    @Schema(example = "Indiranagar, Bangalore, Karnataka, India")
    private String currentAddress;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public Double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }
}
