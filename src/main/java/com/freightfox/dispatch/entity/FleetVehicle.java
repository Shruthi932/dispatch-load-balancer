
package com.freightfox.dispatch.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fleet_vehicles")
public class FleetVehicle {

    @Id
    @Column(name = "vehicle_id", nullable = false, unique = true)
    private String vehicleId;

    @Column(nullable = false)
    private double capacity;

    @Column(nullable = false)
    private double currentLatitude;

    @Column(nullable = false)
    private double currentLongitude;

    @Column(nullable = false)
    private String currentAddress;

    public FleetVehicle() {
    }

    public FleetVehicle(String vehicleId, double capacity,
                        double currentLatitude, double currentLongitude,
                        String currentAddress) {
        this.vehicleId = vehicleId;
        this.capacity = capacity;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.currentAddress = currentAddress;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }
}
