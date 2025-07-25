package com.estapar.domain.garage.sector

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.instanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.time.LocalTime

@ExtendWith(MockitoExtension::class)
class SectorServiceTest {

    @Mock private lateinit var repository: SectorRepository
    private val sectorCaptor = argumentCaptor<Sector>()
    private lateinit var service: SectorService

    @BeforeEach
    fun setUp() {
        service = SectorService(repository)
    }

    @Test
    fun shouldAskSectorRepositoryToSaveSectorWhenSavingSector() {
        val openHour = LocalTime.of(10, 0)
        val closeHour = LocalTime.of(22, 15)
        val sector = Sector(name = "A", basePrice = BigDecimal.valueOf(20.0), maxCapacity = 100, durationLimitMinutes = 10, openHour = openHour, closeHour = closeHour)
        `when`(repository.save(sector = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.saveSectors(listOf(sector)))
            .verifyComplete()

        verify(repository, atLeastOnce()).save(sectorCaptor.capture())
    }

    @Test
    fun shouldAskTwiceSectorRepositoryToSaveSectorWhenSavingTwoSectors() {
        val openHour = LocalTime.of(10, 0)
        val closeHour = LocalTime.of(22, 15)
        val sector1 = Sector(name = "A", basePrice = BigDecimal.valueOf(20.0), maxCapacity = 100, durationLimitMinutes = 10, openHour = openHour, closeHour = closeHour)
        val sector2 = Sector(name = "B", basePrice = BigDecimal.valueOf(14.5), maxCapacity = 30, durationLimitMinutes = 155, openHour = openHour, closeHour = closeHour)
        `when`(repository.save(sector = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.saveSectors(listOf(sector1, sector2)))
            .verifyComplete()

        verify(repository, times(2)).save(sectorCaptor.capture())
    }

    @Test
    fun shouldReturnSavedSectorFromRepositoryWhenSavingSector() {
        val openHour = LocalTime.of(10, 0)
        val closeHour = LocalTime.of(22, 15)
        val sector = Sector(name = "A", basePrice = BigDecimal.valueOf(20.0), maxCapacity = 100, durationLimitMinutes = 10, openHour = openHour, closeHour = closeHour)
        `when`(repository.save(sector = any())).thenReturn(Mono.just(sector))

        StepVerifier.create(service.saveSectors(listOf(sector)))
            .assertNext { sector ->
                assertThat(sector,allOf(
                    instanceOf(Sector::class.java),
                    hasProperty("name", equalTo(sector.name)),
                    hasProperty("basePrice", equalTo(sector.basePrice)),
                    hasProperty("openHour", equalTo(sector.openHour)),
                    hasProperty("closeHour", equalTo(sector.closeHour)),
                    hasProperty("maxCapacity", equalTo(sector.maxCapacity)),
                    hasProperty("durationLimitMinutes", equalTo(sector.durationLimitMinutes))
                ))
            }
            .verifyComplete()
    }

}