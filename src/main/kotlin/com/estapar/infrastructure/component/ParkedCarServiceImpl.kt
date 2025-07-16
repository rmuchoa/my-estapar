package com.estapar.infrastructure.component

import com.estapar.domain.park.ParkedCarRepository
import com.estapar.domain.park.ParkedCarService
import com.estapar.domain.garage.sector.SectorService
import com.estapar.domain.garage.spot.SpotService
import org.springframework.stereotype.Service

@Service
class ParkedCarServiceImpl(
    repository: ParkedCarRepository,
    spotService: SpotService,
    sectorService: SectorService
) : ParkedCarService(repository, spotService, sectorService)