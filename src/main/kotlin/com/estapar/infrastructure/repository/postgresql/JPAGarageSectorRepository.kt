package com.estapar.infrastructure.repository.postgresql

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface JPAGarageSectorRepository : ReactiveCrudRepository<GarageSectorEntity, Long> {
    fun findByName(name: String): Mono<GarageSectorEntity>
}