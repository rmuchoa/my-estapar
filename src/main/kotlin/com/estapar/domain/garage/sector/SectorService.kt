package com.estapar.domain.garage.sector

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

open class SectorService(
    private val repository: SectorRepository
) {

    fun saveSectors(sectors: List<Sector>): Flux<Sector> =
        Flux.fromIterable(sectors)
            .flatMap { sector -> repository.save(sector) }

    fun closeSectorOperation(sector: Sector): Mono<Sector> =
        repository.save(sector = sector.copy(status = SectorStatus.CLOSED))

}