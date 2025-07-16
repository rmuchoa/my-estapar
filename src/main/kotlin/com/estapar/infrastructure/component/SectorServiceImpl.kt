package com.estapar.infrastructure.component

import com.estapar.domain.garage.sector.SectorRepository
import com.estapar.domain.garage.sector.SectorService
import org.springframework.stereotype.Service

@Service
class SectorServiceImpl(
    repository: SectorRepository
) : SectorService(repository)