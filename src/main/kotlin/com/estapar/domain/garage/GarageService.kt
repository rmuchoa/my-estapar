package com.estapar.domain.garage

import com.estapar.domain.garage.sector.SectorService
import com.estapar.domain.garage.spot.SpotService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

open class GarageService(
    private val externalRepository: GarageExternalRepository,
    private val sectorService: SectorService,
    private val spotService: SpotService
) {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    fun loadGarage(): Mono<Garage> =
        externalRepository.loadGarageFirstTime()
            .doOnNext { log.info("Dados carregados: ${it.sectors.size} setores, ${it.spots.size} vagas") }
            .flatMap { garage ->
                sectorService.saveSectors(sectors = garage.sectors)
                    .collectList()
                    .filter { it.isNotEmpty() }
                    .flatMap { sectors ->
                        val appliedGarage = garage.applySpotSectors(sectors)
                        spotService.saveSpots(spots = appliedGarage.spots)
                            .collectList()
                            .filter { it.isNotEmpty() }
                    }
                    .map { garage }
            }

}