package com.estapar.infrastructure.component

import com.estapar.domain.car.logging.GarageLoggingService
import com.estapar.domain.car.park.ParkingService
import com.estapar.domain.garage.spot.SpotReportService
import com.estapar.domain.garage.spot.SpotService
import org.springframework.stereotype.Service

@Service
class SpotReportServiceImpl(
    spotService: SpotService,
    parkingService: ParkingService,
    loggingService: GarageLoggingService
) : SpotReportService(spotService, parkingService, loggingService)