
package com.freightfox.dispatch.service;

import com.freightfox.dispatch.dto.request.DeliveryOrderRequest;
import com.freightfox.dispatch.dto.request.VehicleRequest;
import com.freightfox.dispatch.entity.DeliveryOrder;
import com.freightfox.dispatch.entity.FleetVehicle;
import com.freightfox.dispatch.repository.DeliveryOrderRepository;
import com.freightfox.dispatch.repository.FleetVehicleRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DispatchInputService {

    private final DeliveryOrderRepository orderRepository;
    private final FleetVehicleRepository vehicleRepository;

    public DispatchInputService(
            DeliveryOrderRepository orderRepository,
            FleetVehicleRepository vehicleRepository) {

        this.orderRepository = orderRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public void saveOrders(List<DeliveryOrderRequest> orders) {

        List<DeliveryOrder> orderEntities = new ArrayList<>();

        for (DeliveryOrderRequest request : orders) {

            DeliveryOrder order = new DeliveryOrder(
                    request.getOrderId(),
                    request.getLatitude(),
                    request.getLongitude(),
                    request.getAddress(),
                    request.getPackageWeight(),
                    request.getPriority()
            );

            orderEntities.add(order);
        }

        orderRepository.saveAll(orderEntities);
    }

    @Transactional
    public void saveVehicles(List<VehicleRequest> vehicles) {

        List<FleetVehicle> vehicleEntities = new ArrayList<>();

        for (VehicleRequest request : vehicles) {

            FleetVehicle vehicle = new FleetVehicle(
                    request.getVehicleId(),
                    request.getCapacity(),
                    request.getCurrentLatitude(),
                    request.getCurrentLongitude(),
                    request.getCurrentAddress()
            );

            vehicleEntities.add(vehicle);
        }

        vehicleRepository.saveAll(vehicleEntities);
    }
}
