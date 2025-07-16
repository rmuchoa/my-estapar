package com.estapar.infrastructure.component

import com.estapar.domain.car.park.ParkAttemptService
import com.estapar.domain.car.park.ParkingService
import com.estapar.domain.garage.spot.SpotService
import org.springframework.stereotype.Service

@Service
class ParkAttemptServiceImpl(
    spotService: SpotService,
    parkingService: ParkingService,
) : ParkAttemptService(spotService, parkingService)