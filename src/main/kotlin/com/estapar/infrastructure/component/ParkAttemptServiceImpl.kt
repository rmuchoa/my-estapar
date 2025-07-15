package com.estapar.infrastructure.component

import com.estapar.domain.garage.carEntry.CarEntryService
import com.estapar.domain.garage.park.ParkAttemptService
import com.estapar.domain.garage.park.ParkedCarService
import com.estapar.domain.garage.spot.SpotService
import org.springframework.stereotype.Service

@Service
class ParkAttemptServiceImpl(
    spotService: SpotService,
    parkedCarService: ParkedCarService,
    carEntryService: CarEntryService
) : ParkAttemptService(spotService, parkedCarService, carEntryService)