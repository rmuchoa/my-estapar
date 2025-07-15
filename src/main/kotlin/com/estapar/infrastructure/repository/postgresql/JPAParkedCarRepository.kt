package com.estapar.infrastructure.repository.postgresql

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface JPAParkedCarRepository : ReactiveCrudRepository<ParkedCarEntity, Long>