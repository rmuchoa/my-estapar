package com.estapar.domain.garage.spot

import reactor.core.publisher.Flux

open class SpotService(
    private val repository: SpotRepository
) {

    fun saveSpots(spots: List<Spot>): Flux<Spot> =
        Flux.fromIterable(spots)
            .flatMap { spot -> repository.save(spot) }

}