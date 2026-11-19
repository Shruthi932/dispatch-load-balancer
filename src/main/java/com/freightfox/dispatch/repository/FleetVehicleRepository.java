
package com.freightfox.dispatch.repository;

import com.freightfox.dispatch.entity.FleetVehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FleetVehicleRepository
        extends JpaRepository<FleetVehicle, String> {
}
