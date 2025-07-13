package com.estapar.infrastructure.component

import com.estapar.domain.garage.GarageExternalRepository
import com.estapar.domain.garage.GarageService
import com.estapar.domain.garage.sector.SectorService
import com.estapar.domain.garage.spot.SpotService
import org.springframework.stereotype.Service

@Service
class GarageServiceImpl(
    externalRepository: GarageExternalRepository,
    sectorService: SectorService,
    spotService: SpotService
) : GarageService(externalRepository, sectorService, spotService)