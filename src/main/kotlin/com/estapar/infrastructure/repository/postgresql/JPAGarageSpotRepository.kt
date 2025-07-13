package com.estapar.infrastructure.repository.postgresql

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface JPAGarageSpotRepository : ReactiveCrudRepository<GarageSpotEntity, Long>