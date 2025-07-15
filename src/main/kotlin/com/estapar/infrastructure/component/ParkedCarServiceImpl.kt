package com.estapar.infrastructure.component

import com.estapar.domain.garage.park.ParkedCarRepository
import com.estapar.domain.garage.park.ParkedCarService
import com.estapar.domain.garage.spot.SpotService
import org.springframework.stereotype.Service

@Service
class ParkedCarServiceImpl(
    repository: ParkedCarRepository,
    spotService: SpotService
) : ParkedCarService(repository, spotService)