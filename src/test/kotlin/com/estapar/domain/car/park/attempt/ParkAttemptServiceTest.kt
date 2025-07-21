package com.estapar.domain.car.park.attempt

import com.estapar.AbstractEstaparTest
import com.estapar.domain.car.park.DynamicPriceRule
import com.estapar.domain.car.park.Parking
import com.estapar.domain.car.park.ParkingService
import com.estapar.domain.car.park.ParkingStatus
import com.estapar.domain.garage.spot.SpotService
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class ParkAttemptServiceTest(
    @Mock private val spotService: SpotService,
    @Mock private val parkingService: ParkingService
) : AbstractEstaparTest() {

    private val parkingCaptor = argumentCaptor<Parking>()
    private lateinit var service: ParkAttemptService

    @BeforeEach
    fun setUp() {
        service = ParkAttemptService(spotService, parkingService)
    }

    @Test
    fun shouldAskSpotServiceToFindSpotByLatitudeAndLongitudeWhenAttemptingPark() {
        val parkAttempt = buildSomeParkAttempt()
        `when`(spotService.findSpotBy(
            latitude = eq(parkAttempt.latitude),
            longitude = eq(parkAttempt.longitude))).thenReturn(Mono.empty())
        `when`(parkingService.parkCarOnSpot(parking = any())).thenReturn(Mono.empty())

        service.attemptPark(parkAttempt = parkAttempt)

        verify(spotService, atLeastOnce()).findSpotBy(
            latitude = eq(parkAttempt.latitude),
            longitude = eq(parkAttempt.longitude))
    }

    @Test
    fun shouldAskParkingServiceToParkCarOnSpotWhenFindSomeSpotByLatitudeAndLongitude() {
        val spot = buildSomeSpot()
        val parkAttempt = buildSomeParkAttempt()
        `when`(spotService.findSpotBy(
            latitude = eq(parkAttempt.latitude),
            longitude = eq(parkAttempt.longitude))).thenReturn(Mono.just(spot))
        `when`(parkingService.parkCarOnSpot(parking = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.attemptPark(parkAttempt = parkAttempt)).verifyComplete()

        verify(parkingService, atLeastOnce()).parkCarOnSpot(parking = parkingCaptor.capture())

        assertThat(parkingCaptor.firstValue,allOf(
            instanceOf(Parking::class.java),
            hasProperty("id", nullValue()),
            hasProperty("spot", equalTo(spot)),
            hasProperty("licensePlate", equalTo(parkAttempt.licensePlate)),
            hasProperty("parkingTime", instanceOf<LocalDateTime>(LocalDateTime::class.java)),
            hasProperty("priceRule", equalTo(DynamicPriceRule.TWENTY_FIVE_PERCENT_CAPACITY)),
            hasProperty("status", equalTo(ParkingStatus.ENTERED)),
            hasProperty("unparkingTime", nullValue())
        ))
    }

    @Test
    fun shouldReturnParkedCarOnFoundSpotWhenFindSomeSpotByLatitudeAndLongitude() {
        val spot = buildSomeSpot()
        val parking = buildSomeParking(spot = spot)
        val parkAttempt = buildSomeParkAttempt()
        `when`(spotService.findSpotBy(
            latitude = eq(parkAttempt.latitude),
            longitude = eq(parkAttempt.longitude))).thenReturn(Mono.just(spot))
        `when`(parkingService.parkCarOnSpot(parking = any())).thenReturn(Mono.just(parking))

        StepVerifier.create(service.attemptPark(parkAttempt = parkAttempt))
            .assertNext { parking ->
                assertThat(parking,allOf(
                    instanceOf(Parking::class.java),
                    hasProperty("id", equalTo(parking.id)),
                    hasProperty("spot", equalTo(spot)),
                    hasProperty("licensePlate", equalTo(parking.licensePlate)),
                    hasProperty("parkingTime", equalTo(parking.parkingTime)),
                    hasProperty("priceRule", equalTo(parking.priceRule)),
                    hasProperty("status", equalTo(parking.status)),
                    hasProperty("unparkingTime", nullValue())
                ))
            }
            .verifyComplete()
    }

}