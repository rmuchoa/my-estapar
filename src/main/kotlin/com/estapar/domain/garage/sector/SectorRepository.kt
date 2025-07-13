package com.estapar.domain.garage.sector

import reactor.core.publisher.Mono

interface SectorRepository {
    fun save(sector: Sector): Mono<Sector>
}