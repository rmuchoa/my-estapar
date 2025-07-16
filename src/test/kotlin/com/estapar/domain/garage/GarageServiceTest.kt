package com.estapar.domain.garage

import com.estapar.domain.garage.sector.Sector
import com.estapar.domain.garage.sector.SectorService
import com.estapar.domain.garage.spot.Spot
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
import java.math.BigDecimal
import java.time.LocalTime

@ExtendWith(MockitoExtension::class)
class GarageServiceTest {

    @Mock private lateinit var externalRepository: GarageExternalRepository
    @Mock private lateinit var sectorService: SectorService
    @Mock private lateinit var spotService: SpotService
    private lateinit var service: GarageService

    @BeforeEach
    fun setUp() {
        service = GarageService(externalRepository, sectorService, spotService)
    }

    @Test
    fun shouldAskGarageExternalRepositoryToGetFirstGarageLoad() {
        `when`(externalRepository.loadGarageFirstTime()).thenReturn(Mono.empty())

        StepVerifier.create(service.loadGarage())
            .verifyComplete()

        verify(externalRepository, atLeastOnce()).loadGarageFirstTime()
    }

    @Test
    fun shouldAskSectorServiceToSaveGarageSectorWhenReceivedExternalDataHasAtLeastOnceGarageSector() {
        val sector = Sector(
            name = "A",
            basePrice = BigDecimal.valueOf(12.3),
            openHour = LocalTime.of(9, 0),
            closeHour = LocalTime.of(8, 30),
            durationLimitMinutes = 40,
            maxCapacity = 100)
        `when`(externalRepository.loadGarageFirstTime())
            .thenReturn(Mono.just(Garage(sectors = listOf(sector), spots = emptyList())))
        `when`(sectorService.saveSectors(sectors = anyList())).thenReturn(Flux.empty())

        StepVerifier.create(service.loadGarage())
            .verifyComplete()

        verify(sectorService, atLeastOnce()).saveSectors(sectors = listOf(sector))
    }

    @Test
    fun shouldNeverAskSpotServiceToSaveGarageSpotWhenReceiveNoOneFluxFromSavingSectors() {
        val sector = Sector(
            name = "A",
            basePrice = BigDecimal.valueOf(12.3),
            openHour = LocalTime.of(9, 0),
            closeHour = LocalTime.of(8, 30),
            durationLimitMinutes = 40,
            maxCapacity = 100)
        `when`(externalRepository.loadGarageFirstTime())
            .thenReturn(Mono.just(Garage(sectors = listOf(sector), spots = emptyList())))
        `when`(sectorService.saveSectors(sectors = anyList())).thenReturn(Flux.empty())

        StepVerifier.create(service.loadGarage())
            .verifyComplete()

        verify(sectorService, atLeastOnce()).saveSectors(sectors = listOf(sector))
        verify(spotService, never()).saveSpots(spots = anyList())
    }

    @Test
    fun shouldSendAllReceivedSectorsToSaveWhenReceiveMoreThanOneGarageSectorOnGarageFromExternalData() {
        val sector1 = Sector(
            name = "A",
            basePrice = BigDecimal.valueOf(12.3),
            openHour = LocalTime.of(9, 0),
            closeHour = LocalTime.of(8, 30),
            durationLimitMinutes = 40,
            maxCapacity = 100)
        val sector2 = Sector(
            name = "A",
            basePrice = BigDecimal.valueOf(12.3),
            openHour = LocalTime.of(9, 0),
            closeHour = LocalTime.of(8, 30),
            durationLimitMinutes = 40,
            maxCapacity = 100)
        `when`(externalRepository.loadGarageFirstTime())
            .thenReturn(Mono.just(Garage(sectors = listOf(sector1, sector2), spots = emptyList())))
        `when`(sectorService.saveSectors(sectors = anyList())).thenReturn(Flux.empty())

        StepVerifier.create(service.loadGarage())
            .verifyComplete()

        verify(sectorService, atLeastOnce()).saveSectors(sectors = listOf(sector1, sector2))
    }

    @Test
    fun shouldAskSpotServiceToSaveGarageSpotWhenReceivedExternalDataHasAtLeastOnceGarageSector() {
        val sector = Sector(
            name = "A",
            basePrice = BigDecimal.valueOf(12.3),
            openHour = LocalTime.of(9, 0),
            closeHour = LocalTime.of(8, 30),
            durationLimitMinutes = 40,
            maxCapacity = 100)
        val spot = Spot(
            sector = sector,
            latitude = 34.0,
            longitude = 52.2,
            occupied = true
        )
        `when`(externalRepository.loadGarageFirstTime())
            .thenReturn(Mono.just(Garage(sectors = listOf(sector), spots = listOf(spot))))
        `when`(sectorService.saveSectors(sectors = anyList())).thenReturn(Flux.just(sector))
        `when`(spotService.saveSpots(spots = anyList())).thenReturn(Flux.empty())

        StepVerifier.create(service.loadGarage())
            .verifyComplete()

        verify(spotService, atLeastOnce()).saveSpots(spots = listOf(spot))
    }

}