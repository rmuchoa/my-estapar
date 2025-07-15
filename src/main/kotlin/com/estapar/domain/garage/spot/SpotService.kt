package com.estapar.domain.garage.spot

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

open class SpotService(
    private val repository: SpotRepository
) {

    fun saveSpots(spots: List<Spot>): Flux<Spot> =
        Flux.fromIterable(spots)
            .flatMap { spot -> repository.save(spot) }

    fun findSpotBy(latitude: Double, longitude: Double): Mono<Spot> =
        repository.findByGeoposition(latitude, longitude)
            .switchIfEmpty(Mono.error {
                NotFoundSpotException(
                    message = "No one spot was found for latitude: '$latitude' and longitude: '$longitude'")
            })

    fun saveSpot(spot: Spot): Mono<Spot> =
        repository.save(spot)

}