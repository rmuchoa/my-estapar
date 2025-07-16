package com.estapar.infrastructure.repository.postgresql

import com.estapar.infrastructure.repository.postgresql.entity.GarageSectorEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface JPAGarageSectorRepository : ReactiveCrudRepository<GarageSectorEntity, Long>