
package com.freightfox.dispatch.controller;

import com.freightfox.dispatch.dto.response.DispatchPlanResponse;
import com.freightfox.dispatch.service.DispatchOptimizationService;

import com.freightfox.dispatch.dto.request.OrdersRequest;
import com.freightfox.dispatch.dto.request.VehiclesRequest;
import com.freightfox.dispatch.dto.response.ApiResponse;
import com.freightfox.dispatch.service.DispatchInputService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dispatch")
public class DispatchController {


    private final DispatchInputService dispatchInputService;

    private final DispatchOptimizationService dispatchOptimizationService;


    public DispatchController(
            DispatchInputService dispatchInputService,
            DispatchOptimizationService dispatchOptimizationService) {

        this.dispatchInputService = dispatchInputService;
        this.dispatchOptimizationService = dispatchOptimizationService;
    }

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse> addOrders(
            @Valid @RequestBody OrdersRequest request) {

        dispatchInputService.saveOrders(request.getOrders());

        ApiResponse response = new ApiResponse(
                "Delivery orders accepted.",
                "success"
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/vehicles")
    public ResponseEntity<ApiResponse> addVehicles(
            @Valid @RequestBody VehiclesRequest request) {

        dispatchInputService.saveVehicles(request.getVehicles());

        ApiResponse response = new ApiResponse(
                "Vehicle details accepted.",
                "success"
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/plan")
    public ResponseEntity<DispatchPlanResponse> getDispatchPlan() {

        DispatchPlanResponse response =
                dispatchOptimizationService.generateDispatchPlan();

        return ResponseEntity.ok(response);
    }
}


