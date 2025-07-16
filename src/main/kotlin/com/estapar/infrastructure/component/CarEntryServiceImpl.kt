package com.estapar.infrastructure.component

import com.estapar.domain.car.entry.CarEntryService
import com.estapar.domain.car.logging.GarageLoggingService
import org.springframework.stereotype.Service

@Service
class CarEntryServiceImpl(
    garageLoggingService: GarageLoggingService
) : CarEntryService(garageLoggingService)