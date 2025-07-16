package com.estapar.infrastructure.component

import com.estapar.domain.car.entry.CarEntryService
import com.estapar.domain.car.park.ParkAttemptService
import com.estapar.domain.car.park.ParkedCarService
import com.estapar.domain.garage.spot.SpotService
import org.springframework.stereotype.Service

@Service
class ParkAttemptServiceImpl(
    spotService: SpotService,
    parkedCarService: ParkedCarService,
    carEntryService: CarEntryService
) : ParkAttemptService(spotService, parkedCarService, carEntryService)