package com.estapar.infrastructure.api

import com.estapar.domain.car.logging.entry.CarEntryService
import com.estapar.domain.car.logging.exit.CarExitService
import com.estapar.domain.car.park.attempt.ParkAttemptService
import com.estapar.infrastructure.api.dto.GarageEntryEvent
import com.estapar.infrastructure.api.dto.GarageExitEvent
import com.estapar.infrastructure.api.dto.GarageParkedEvent
import com.estapar.infrastructure.api.dto.GarageWebhookEvent
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/webhook")
class GarageWebhookController(
    val carEntryService: CarEntryService,
    val parkAttemptService: ParkAttemptService,
    val carExitService: CarExitService
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping
    fun receiveEvent(@RequestBody event: GarageWebhookEvent): Mono<Void> =
        when(event) {
            is GarageEntryEvent -> { receiveEntryEvent(entryEvent = event) }
            is GarageParkedEvent -> { receiveParkedEvent(parkedEvent = event) }
            is GarageExitEvent -> { receiveExitEvent(exitEvent = event) }
            else -> { Mono.empty() }
        }.then()

    fun receiveEntryEvent(entryEvent: GarageEntryEvent) =
        Mono.just(entryEvent)
            .doOnNext { event -> log.info("Entrada: ${event.licensePlate} at ${event.entryTime}") }
            .map { event -> event.toDomain() }
            .flatMap { carEntry -> carEntryService.logEntry(carEntry) }

    fun receiveParkedEvent(parkedEvent: GarageParkedEvent) =
        Mono.just(parkedEvent)
            .doOnNext { event -> log.info("Entrada: ${event.licensePlate} at em ${event.latitude}, ${event.longitude}") }
            .map { event -> event.toDomain() }
            .flatMap { parkAttempt -> parkAttemptService.attemptPark(parkAttempt) }

    fun receiveExitEvent(exitEvent: GarageExitEvent) =
        Mono.just(exitEvent)
            .doOnNext { event -> log.info("Entrada: ${event.licensePlate} at ${event.exitTime}") }
            .map { event -> event.toDomain() }
            .flatMap { carExit -> carExitService.logExit(carExit) }

}