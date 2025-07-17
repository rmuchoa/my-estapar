package com.estapar.infrastructure.component

import com.estapar.domain.car.billing.BillingService
import com.estapar.domain.car.logging.exit.CarExitService
import com.estapar.domain.car.logging.GarageLoggingService
import com.estapar.domain.car.park.ParkingService
import org.springframework.stereotype.Service

@Service
class CarExitServiceImpl(
    garageLoggingService: GarageLoggingService,
    parkingService: ParkingService,
    billingService: BillingService
) : CarExitService(garageLoggingService, parkingService, billingService)