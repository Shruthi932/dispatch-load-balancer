
package com.freightfox.dispatch.service;

import com.freightfox.dispatch.dto.response.AssignedOrderResponse;
import com.freightfox.dispatch.dto.response.DispatchPlanResponse;
import com.freightfox.dispatch.dto.response.VehicleDispatchResponse;
import com.freightfox.dispatch.entity.DeliveryOrder;
import com.freightfox.dispatch.entity.FleetVehicle;
import com.freightfox.dispatch.repository.DeliveryOrderRepository;
import com.freightfox.dispatch.repository.FleetVehicleRepository;
import com.freightfox.dispatch.util.DistanceCalculator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class DispatchOptimizationService {

    private static final double CAPACITY_TOLERANCE = 0.000000001;

    private final DeliveryOrderRepository orderRepository;
    private final FleetVehicleRepository vehicleRepository;

    public DispatchOptimizationService(
            DeliveryOrderRepository orderRepository,
            FleetVehicleRepository vehicleRepository) {

        this.orderRepository = orderRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional(readOnly = true)
    public DispatchPlanResponse generateDispatchPlan() {

        List<DeliveryOrder> orders = orderRepository.findAll();
        List<FleetVehicle> vehicles = vehicleRepository.findAll();

        // Sort orders: HIGH first, then MEDIUM, then LOW.
        orders.sort(
                Comparator.comparingInt(
                        order -> order.getPriority().ordinal()
                )
        );

        // Sort vehicles by ID for consistent tie-breaking.
        vehicles.sort(
                Comparator.comparing(FleetVehicle::getVehicleId)
        );

        // Create a working state for every vehicle.
        List<VehicleState> vehicleStates = new ArrayList<>();

        for (FleetVehicle vehicle : vehicles) {
            vehicleStates.add(new VehicleState(vehicle));
        }

        List<String> unassignedOrders = new ArrayList<>();

        // Assign each order to the nearest vehicle with enough capacity.
        for (DeliveryOrder order : orders) {

            VehicleState bestVehicle = null;
            double shortestDistance = Double.MAX_VALUE;

            for (VehicleState state : vehicleStates) {

                double remainingCapacity =
                        state.capacity - state.totalLoad;

                if (remainingCapacity + CAPACITY_TOLERANCE
                        < order.getPackageWeight()) {
                    continue;
                }

                double distance = DistanceCalculator.calculateDistance(
                        state.currentLatitude,
                        state.currentLongitude,
                        order.getLatitude(),
                        order.getLongitude()
                );

                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    bestVehicle = state;
                }
            }

            if (bestVehicle == null) {
                unassignedOrders.add(order.getOrderId());
                continue;
            }

            // Assign the order to the selected vehicle.
            bestVehicle.assignedOrders.add(
                    new AssignedOrderResponse(
                            order.getOrderId(),
                            order.getAddress(),
                            order.getPackageWeight(),
                            order.getPriority()
                    )
            );

            // Update the vehicle's load and route.
            bestVehicle.totalLoad += order.getPackageWeight();
            bestVehicle.totalDistance += shortestDistance;

            // The next delivery starts from this order's location.
            bestVehicle.currentLatitude = order.getLatitude();
            bestVehicle.currentLongitude = order.getLongitude();
        }

        // Prepare the final API response.
        List<VehicleDispatchResponse> dispatchPlan =
                new ArrayList<>();

        for (VehicleState state : vehicleStates) {

            String formattedDistance = String.format(
                    Locale.US,
                    "%.2f km",
                    state.totalDistance
            );

            dispatchPlan.add(
                    new VehicleDispatchResponse(
                            state.vehicleId,
                            state.capacity,
                            state.totalLoad,
                            formattedDistance,
                            state.assignedOrders
                    )
            );
        }

        return new DispatchPlanResponse(
                dispatchPlan,
                unassignedOrders
        );
    }

    // Temporary working information for each vehicle.
    private static class VehicleState {

        private final String vehicleId;
        private final double capacity;

        private double currentLatitude;
        private double currentLongitude;
        private double totalLoad;
        private double totalDistance;

        private final List<AssignedOrderResponse> assignedOrders =
                new ArrayList<>();

        private VehicleState(FleetVehicle vehicle) {

            this.vehicleId = vehicle.getVehicleId();
            this.capacity = vehicle.getCapacity();

            this.currentLatitude = vehicle.getCurrentLatitude();
            this.currentLongitude = vehicle.getCurrentLongitude();

            this.totalLoad = 0.0;
            this.totalDistance = 0.0;
        }
    }
}
