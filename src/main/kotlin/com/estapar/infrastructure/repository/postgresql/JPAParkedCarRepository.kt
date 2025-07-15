package com.estapar.infrastructure.repository.postgresql

import com.estapar.infrastructure.repository.postgresql.entity.ParkedCarEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface JPAParkedCarRepository : ReactiveCrudRepository<ParkedCarEntity, Long>