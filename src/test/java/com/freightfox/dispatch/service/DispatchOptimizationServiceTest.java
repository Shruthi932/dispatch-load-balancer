
package com.freightfox.dispatch.service;

import com.freightfox.dispatch.dto.response.DispatchPlanResponse;
import com.freightfox.dispatch.dto.response.VehicleDispatchResponse;
import com.freightfox.dispatch.entity.DeliveryOrder;
import com.freightfox.dispatch.entity.FleetVehicle;
import com.freightfox.dispatch.enums.OrderPriority;
import com.freightfox.dispatch.repository.DeliveryOrderRepository;
import com.freightfox.dispatch.repository.FleetVehicleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DispatchOptimizationServiceTest {

    private DeliveryOrderRepository orderRepository;
    private FleetVehicleRepository vehicleRepository;
    private DispatchOptimizationService dispatchService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(DeliveryOrderRepository.class);
        vehicleRepository = mock(FleetVehicleRepository.class);

        dispatchService = new DispatchOptimizationService(
                orderRepository,
                vehicleRepository
        );
    }

    @Test
    void shouldAssignHighPriorityOrderFirst() {

        FleetVehicle vehicle = new FleetVehicle(
                "VEH001", 20.0, 28.6139, 77.2090, "Delhi"
        );

        DeliveryOrder lowOrder = new DeliveryOrder(
                "ORD002", 28.6139, 77.2090,
                "Noida", 5.0, OrderPriority.LOW
        );

        DeliveryOrder highOrder = new DeliveryOrder(
                "ORD001", 28.6139, 77.2090,
                "Delhi", 5.0, OrderPriority.HIGH
        );

        when(orderRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(lowOrder, highOrder))
        );

        when(vehicleRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(vehicle))
        );

        DispatchPlanResponse response =
                dispatchService.generateDispatchPlan();

        VehicleDispatchResponse vehicleResponse =
                response.getDispatchPlan().get(0);

        assertEquals(2, vehicleResponse.getAssignedOrders().size());

        assertEquals(
                OrderPriority.HIGH,
                vehicleResponse.getAssignedOrders().get(0).getPriority()
        );

        assertEquals(
                OrderPriority.LOW,
                vehicleResponse.getAssignedOrders().get(1).getPriority()
        );
    }

    @Test
    void shouldNotExceedVehicleCapacity() {

        FleetVehicle vehicle = new FleetVehicle(
                "VEH001", 10.0, 28.6139, 77.2090, "Delhi"
        );

        DeliveryOrder order = new DeliveryOrder(
                "ORD001", 28.6139, 77.2090,
                "Delhi", 15.0, OrderPriority.HIGH
        );

        when(orderRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(order))
        );

        when(vehicleRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(vehicle))
        );

        DispatchPlanResponse response =
                dispatchService.generateDispatchPlan();

        VehicleDispatchResponse vehicleResponse =
                response.getDispatchPlan().get(0);

        assertEquals(0, vehicleResponse.getAssignedOrders().size());
        assertEquals(0.0, vehicleResponse.getTotalLoad());

        assertTrue(response.getUnassignedOrders().contains("ORD001"));
    }

    @Test
    void shouldAssignOrderToNearestVehicle() {

        FleetVehicle vehicle1 = new FleetVehicle(
                "VEH001", 100.0, 28.6139, 77.2090, "Delhi"
        );

        FleetVehicle vehicle2 = new FleetVehicle(
                "VEH002", 100.0, 28.5355, 77.3910, "Noida"
        );

        DeliveryOrder order = new DeliveryOrder(
                "ORD001", 28.5355, 77.3910,
                "Noida Sector 18", 10.0, OrderPriority.HIGH
        );

        when(orderRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(order))
        );

        when(vehicleRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(vehicle1, vehicle2))
        );

        DispatchPlanResponse response =
                dispatchService.generateDispatchPlan();

        VehicleDispatchResponse firstVehicle =
                response.getDispatchPlan().get(0);

        VehicleDispatchResponse secondVehicle =
                response.getDispatchPlan().get(1);

        assertEquals("VEH001", firstVehicle.getVehicleId());
        assertEquals("VEH002", secondVehicle.getVehicleId());

        assertEquals(0, firstVehicle.getAssignedOrders().size());

        assertEquals(
                "ORD001",
                secondVehicle.getAssignedOrders().get(0).getOrderId()
        );

        assertTrue(response.getUnassignedOrders().isEmpty());
    }

    @Test
    void shouldReturnEmptyPlanWhenNoOrdersExist() {

        FleetVehicle vehicle = new FleetVehicle(
                "VEH001", 100.0, 28.6139, 77.2090, "Delhi"
        );

        when(orderRepository.findAll()).thenReturn(new ArrayList<>());
        when(vehicleRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(vehicle))
        );

        DispatchPlanResponse response =
                dispatchService.generateDispatchPlan();

        assertTrue(response.getDispatchPlan().get(0)
                .getAssignedOrders().isEmpty());

        assertTrue(response.getUnassignedOrders().isEmpty());
    }

    @Test
    void shouldMarkAllOrdersUnassignedWhenNoVehiclesExist() {

        DeliveryOrder order = new DeliveryOrder(
                "ORD001", 28.6139, 77.2090,
                "Delhi", 10.0, OrderPriority.HIGH
        );

        when(orderRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(order))
        );

        when(vehicleRepository.findAll()).thenReturn(new ArrayList<>());

        DispatchPlanResponse response =
                dispatchService.generateDispatchPlan();

        assertTrue(response.getDispatchPlan().isEmpty());
        assertTrue(response.getUnassignedOrders().contains("ORD001"));
    }

    @Test
    void shouldNotExceedCapacityWhenMultipleOrdersCompeteForVehicle() {

        FleetVehicle vehicle = new FleetVehicle(
                "VEH001", 10.0, 28.6139, 77.2090, "Delhi"
        );

        DeliveryOrder highOrder = new DeliveryOrder(
                "ORD001", 28.6139, 77.2090,
                "Delhi", 7.0, OrderPriority.HIGH
        );

        DeliveryOrder lowOrder = new DeliveryOrder(
                "ORD002", 28.6139, 77.2090,
                "Noida", 5.0, OrderPriority.LOW
        );

        when(orderRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(lowOrder, highOrder))
        );

        when(vehicleRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(vehicle))
        );

        DispatchPlanResponse response =
                dispatchService.generateDispatchPlan();

        VehicleDispatchResponse vehicleResponse =
                response.getDispatchPlan().get(0);

        assertEquals(7.0, vehicleResponse.getTotalLoad(), 0.001);

        assertEquals(1, vehicleResponse.getAssignedOrders().size());

        assertTrue(response.getUnassignedOrders().contains("ORD002"));
    }

    @Test
    void shouldMarkAllOrdersUnassignedWhenTheyExceedVehicleCapacity() {

        FleetVehicle vehicle = new FleetVehicle(
                "VEH001", 10.0, 28.6139, 77.2090, "Delhi"
        );

        DeliveryOrder order1 = new DeliveryOrder(
                "ORD001", 28.6139, 77.2090,
                "Delhi", 15.0, OrderPriority.HIGH
        );

        DeliveryOrder order2 = new DeliveryOrder(
                "ORD002", 28.5355, 77.3910,
                "Noida", 20.0, OrderPriority.MEDIUM
        );

        when(orderRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(order1, order2))
        );

        when(vehicleRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(vehicle))
        );

        DispatchPlanResponse response =
                dispatchService.generateDispatchPlan();

        assertEquals(2, response.getUnassignedOrders().size());

        assertTrue(response.getUnassignedOrders().contains("ORD001"));
        assertTrue(response.getUnassignedOrders().contains("ORD002"));

        assertEquals(
                0.0,
                response.getDispatchPlan().get(0).getTotalLoad()
        );
    }

}
