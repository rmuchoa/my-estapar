package com.estapar.domain.garage

import com.estapar.AbstractEstaparTest
import com.estapar.domain.garage.sector.SectorService
import com.estapar.domain.garage.spot.SpotService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class GarageServiceTest(
    @Mock private val externalRepository: GarageExternalRepository,
    @Mock private val sectorService: SectorService,
    @Mock private val spotService: SpotService
) : AbstractEstaparTest() {

    private lateinit var service: GarageService

    @BeforeEach
    fun setUp() {
        service = GarageService(externalRepository, sectorService, spotService)
    }

    @Test
    fun shouldAskGarageExternalRepositoryToGetFirstGarageLoad() {
        `when`(externalRepository.loadGarageFirstTime()).thenReturn(Mono.empty())

        StepVerifier.create(service.loadGarage()).verifyComplete()

        verify(externalRepository, atLeastOnce()).loadGarageFirstTime()
    }

    @Test
    fun shouldAskSectorServiceToSaveGarageSectorWhenReceivedExternalDataHasAtLeastOnceGarageSector() {
        val sector = buildSomeSector()
        `when`(externalRepository.loadGarageFirstTime())
            .thenReturn(Mono.just(Garage(sectors = listOf(sector), spots = emptyList())))
        `when`(sectorService.saveSectors(sectors = anyList())).thenReturn(Flux.empty())

        StepVerifier.create(service.loadGarage()).verifyComplete()

        verify(sectorService, atLeastOnce()).saveSectors(sectors = listOf(sector))
    }

    @Test
    fun shouldNeverAskSpotServiceToSaveGarageSpotWhenReceiveNoOneFluxFromSavingSectors() {
        val sector = buildSomeSector()
        `when`(externalRepository.loadGarageFirstTime())
            .thenReturn(Mono.just(Garage(sectors = listOf(sector), spots = emptyList())))
        `when`(sectorService.saveSectors(sectors = anyList())).thenReturn(Flux.empty())

        StepVerifier.create(service.loadGarage()).verifyComplete()

        verify(sectorService, atLeastOnce()).saveSectors(sectors = listOf(sector))
        verify(spotService, never()).saveSpots(spots = anyList())
    }

    @Test
    fun shouldSendAllReceivedSectorsToSaveWhenReceiveMoreThanOneGarageSectorOnGarageFromExternalData() {
        val sector1 = buildSomeSector()
        val sector2 = buildSomeSector()
        `when`(externalRepository.loadGarageFirstTime())
            .thenReturn(Mono.just(Garage(sectors = listOf(sector1, sector2), spots = emptyList())))
        `when`(sectorService.saveSectors(sectors = anyList())).thenReturn(Flux.empty())

        StepVerifier.create(service.loadGarage()).verifyComplete()

        verify(sectorService, atLeastOnce()).saveSectors(sectors = listOf(sector1, sector2))
    }

    @Test
    fun shouldAskSpotServiceToSaveGarageSpotWhenReceivedExternalDataHasAtLeastOnceGarageSector() {
        val sector = buildSomeSector()
        val spot = buildSomeSpot(sector = sector)
        `when`(externalRepository.loadGarageFirstTime())
            .thenReturn(Mono.just(Garage(sectors = listOf(sector), spots = listOf(spot))))
        `when`(sectorService.saveSectors(sectors = anyList())).thenReturn(Flux.just(sector))
        `when`(spotService.saveSpots(spots = anyList())).thenReturn(Flux.empty())

        StepVerifier.create(service.loadGarage()).verifyComplete()

        verify(spotService, atLeastOnce()).saveSpots(spots = listOf(spot))
    }

}