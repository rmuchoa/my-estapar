package com.estapar.infrastructure.component

import com.estapar.domain.car.logging.GarageLoggingService
import com.estapar.domain.car.park.ParkingService
import com.estapar.domain.car.park.report.ParkingReportService
import org.springframework.stereotype.Service

@Service
class ParkingReportServiceImpl(
    garageLoggingService: GarageLoggingService,
    parkingService: ParkingService
) : ParkingReportService(garageLoggingService, parkingService)