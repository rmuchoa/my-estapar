package com.estapar.domain.garage.spot

import com.estapar.domain.garage.sector.Sector
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
class SpotServiceTest {

    @Mock private lateinit var repository: SpotRepository
    private val spotCaptor = argumentCaptor<Spot>()
    private lateinit var service: SpotService

    @BeforeEach
    fun setUp() {
        service = SpotService(repository)
    }

    @Test
    fun shouldAskSpotRepositoryToSaveSpotWhenSavingSpot() {
        val openHour = LocalTime.of(10, 0)
        val closeHour = LocalTime.of(22, 15)
        val sector = Sector(name = "A", basePrice = BigDecimal.valueOf(20.0), maxCapacity = 100, durationLimitMinutes = 10, openHour = openHour, closeHour = closeHour)
        val spot = Spot(sector = sector, latitude = 39.22, longitude = 45.23, occupied = true)
        `when`(repository.save(spot = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.saveSpots(listOf(spot)))
            .verifyComplete()

        verify(repository, atLeastOnce()).save(spotCaptor.capture())
    }

    @Test
    fun shouldAskTwiceSpotRepositoryToSaveSpotWhenSavingTwoSpots() {
        val openHour = LocalTime.of(10, 0)
        val closeHour = LocalTime.of(22, 15)
        val sector = Sector(
            name = "A",
            basePrice = BigDecimal.valueOf(20.0),
            maxCapacity = 100,
            durationLimitMinutes = 10,
            openHour = openHour,
            closeHour = closeHour
        )
        val spot1 = Spot(
            sector = sector,
            latitude = 39.22,
            longitude = 45.23,
            occupied = true)
        val spot2 = Spot(
            sector = sector,
            latitude = 39.22,
            longitude = 45.23,
            occupied = true)
        `when`(repository.save(spot = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.saveSpots(listOf(spot1, spot2)))
            .verifyComplete()

        verify(repository, times(2)).save(spotCaptor.capture())
    }

    @Test
    fun shouldReturnSavedSectorFromRepositoryWhenSavingSector() {
        val openHour = LocalTime.of(10, 0)
        val closeHour = LocalTime.of(22, 15)
        val sector = Sector(
            name = "A",
            basePrice = BigDecimal.valueOf(20.0),
            maxCapacity = 100,
            durationLimitMinutes = 10,
            openHour = openHour,
            closeHour = closeHour
        )
        val spot = Spot(
            sector = sector,
            latitude = 39.22,
            longitude = 45.23,
            occupied = true)
        `when`(repository.save(spot = any())).thenReturn(Mono.just(spot))

        StepVerifier.create(service.saveSpots(listOf(spot)))
            .assertNext { spot ->
                assertThat(
                    spot, allOf(
                        instanceOf(Spot::class.java),
                        hasProperty("sector", instanceOf<Sector>(Sector::class.java)),
                        hasProperty("latitude", equalTo(spot.latitude)),
                        hasProperty("longitude", equalTo(spot.longitude)),
                        hasProperty("occupied", equalTo(spot.occupied)),
                    )
                )
            }
            .verifyComplete()
    }

}