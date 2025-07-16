package com.estapar.infrastructure.component

import com.estapar.domain.car.park.ParkingRepository
import com.estapar.domain.car.park.ParkingService
import com.estapar.domain.garage.sector.SectorService
import com.estapar.domain.garage.spot.SpotService
import org.springframework.stereotype.Service

@Service
class ParkingServiceImpl(
    repository: ParkingRepository,
    spotService: SpotService,
    sectorService: SectorService
) : ParkingService(repository, spotService, sectorService)