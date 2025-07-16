package com.estapar.infrastructure.component

import com.estapar.domain.garage.spot.SpotRepository
import com.estapar.domain.garage.spot.SpotService
import org.springframework.stereotype.Service

@Service
class SpotServiceImpl(
    repository: SpotRepository
) : SpotService(repository)