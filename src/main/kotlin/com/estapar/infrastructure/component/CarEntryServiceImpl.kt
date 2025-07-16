package com.estapar.infrastructure.component

import com.estapar.domain.car.entry.CarEntryRepository
import com.estapar.domain.car.entry.CarEntryService
import org.springframework.stereotype.Service

@Service
class CarEntryServiceImpl(
    repository: CarEntryRepository
) : CarEntryService(repository)