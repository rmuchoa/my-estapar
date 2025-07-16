package com.estapar.infrastructure.app

import com.estapar.domain.garage.GarageService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class GarageApplicationRunner(
    private val service: GarageService
) : ApplicationRunner {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun run(args: ApplicationArguments?) {
        service.loadGarage()
            .doOnSuccess { log.info("Resultado da carga de garagem: $it") }
            .doOnError { log.error("Erro ao carregar garagem: ${it.message}", it) }
            .block()
    }

}