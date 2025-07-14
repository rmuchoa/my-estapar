package com.estapar.infrastructure.api

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/webhook")
class GarageWebhookController {

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

    fun receiveParkedEvent(parkedEvent: GarageParkedEvent) =
        Mono.just(parkedEvent)
            .doOnNext { event -> log.info("Entrada: ${event.licensePlate} at em ${event.lat}, ${event.lng}") }

    fun receiveExitEvent(exitEvent: GarageExitEvent) =
        Mono.just(exitEvent)
            .doOnNext { event -> log.info("Entrada: ${event.licensePlate} at ${event.exitTime}") }

}