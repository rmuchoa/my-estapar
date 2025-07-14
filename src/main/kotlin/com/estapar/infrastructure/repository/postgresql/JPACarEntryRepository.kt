package com.estapar.infrastructure.repository.postgresql

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface JPACarEntryRepository : ReactiveCrudRepository<CarEntryEntity, Long>