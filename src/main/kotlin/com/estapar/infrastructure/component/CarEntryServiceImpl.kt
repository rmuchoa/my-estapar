package com.estapar.infrastructure.component

import com.estapar.domain.garage.carEntry.CarEntryRepository
import com.estapar.domain.garage.carEntry.CarEntryService
import org.springframework.stereotype.Service

@Service
class CarEntryServiceImpl(
    repository: CarEntryRepository
) : CarEntryService(repository)