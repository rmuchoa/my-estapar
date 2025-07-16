package com.estapar.infrastructure.component

import com.estapar.domain.carEntry.CarEntryRepository
import com.estapar.domain.carEntry.CarEntryService
import org.springframework.stereotype.Service

@Service
class CarEntryServiceImpl(
    repository: CarEntryRepository
) : CarEntryService(repository)