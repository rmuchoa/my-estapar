package com.estapar.domain.car.logging

import reactor.core.publisher.Mono

interface GarageLoggingRepository {
    fun logGarageEntry(garageLogging: GarageLogging): Mono<GarageLogging>
    fun findByLicensePlate(licensePlate: String): Mono<GarageLogging>
    fun findActiveByLicensePlate(licensePlate: String): Mono<GarageLogging>
}