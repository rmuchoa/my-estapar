package com.estapar.domain.garage.sector

import reactor.core.publisher.Flux

open class SectorService(
    private val repository: SectorRepository
) {

    fun saveSectors(sectors: List<Sector>): Flux<Sector> =
        Flux.fromIterable(sectors)
            .flatMap { sector -> repository.save(sector) }

}