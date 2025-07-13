package com.estapar.domain.garage

import reactor.core.publisher.Mono

interface GarageExternalRepository {
    fun loadGarageFirstTime(): Mono<Garage>
}