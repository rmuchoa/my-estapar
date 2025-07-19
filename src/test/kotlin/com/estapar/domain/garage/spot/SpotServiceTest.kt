package com.estapar.domain.garage.spot

import com.estapar.AbstractEstaparTest
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

@ExtendWith(MockitoExtension::class)
class SpotServiceTest(
    @Mock private val repository: SpotRepository
) : AbstractEstaparTest() {

    private val spotCaptor = argumentCaptor<Spot>()
    private lateinit var service: SpotService

    @BeforeEach
    fun setUp() {
        service = SpotService(repository)
    }

    @Test
    fun shouldAskSpotRepositoryToSaveSpotWhenSavingSpot() {
        val spot = buildSomeSpot()
        `when`(repository.save(spot = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.saveSpots(listOf(spot))).verifyComplete()

        verify(repository, atLeastOnce()).save(spotCaptor.capture())

        assertThat(
            spotCaptor.firstValue, allOf(
                instanceOf(Spot::class.java),
                hasProperty("sector", instanceOf<Sector>(Sector::class.java)),
                hasProperty("latitude", equalTo(spot.latitude)),
                hasProperty("longitude", equalTo(spot.longitude)),
                hasProperty("occupied", equalTo(spot.occupied))
            )
        )
    }

    @Test
    fun shouldAskTwiceSpotRepositoryToSaveSpotWhenSavingTwoSpots() {
        val spot1 = buildSomeSpot()
        val spot2 = buildSomeSpot()
        `when`(repository.save(spot = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.saveSpots(listOf(spot1, spot2)))
            .verifyComplete()

        verify(repository, times(2)).save(spotCaptor.capture())

        assertThat(
            spotCaptor.firstValue, allOf(
                instanceOf(Spot::class.java),
                hasProperty("sector", instanceOf<Sector>(Sector::class.java)),
                hasProperty("latitude", equalTo(spot1.latitude)),
                hasProperty("longitude", equalTo(spot1.longitude)),
                hasProperty("occupied", equalTo(spot1.occupied))
            )
        )
        assertThat(
            spotCaptor.secondValue, allOf(
                instanceOf(Spot::class.java),
                hasProperty("sector", instanceOf<Sector>(Sector::class.java)),
                hasProperty("latitude", equalTo(spot2.latitude)),
                hasProperty("longitude", equalTo(spot2.longitude)),
                hasProperty("occupied", equalTo(spot2.occupied))
            )
        )
    }

    @Test
    fun shouldReturnSavedSectorFromRepositoryWhenSavingSector() {
        val spot = buildSomeSpot()
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