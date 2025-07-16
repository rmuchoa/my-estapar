package com.estapar.infrastructure.component

import com.estapar.domain.car.logging.GarageLoggingRepository
import com.estapar.domain.car.logging.GarageLoggingService
import org.springframework.stereotype.Service

@Service
class GarageLoggingServiceImpl(
    repository: GarageLoggingRepository
) : GarageLoggingService(repository)