package com.estapar.domain.car.logging.exit

import com.estapar.AbstractEstaparTest
import com.estapar.domain.car.billing.BillingService
import com.estapar.domain.car.logging.GarageLoggingService
import com.estapar.domain.car.logging.NotFoundGarageLoggingException
import com.estapar.domain.car.park.ParkingService
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.instanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class CarExitServiceTest(
    @Mock private val garageLoggingService: GarageLoggingService,
    @Mock private val parkingService: ParkingService,
    @Mock private val billingService: BillingService
) : AbstractEstaparTest() {

    private lateinit var service: CarExitService

    @BeforeEach
    fun setUp() {
        service = CarExitService(garageLoggingService, parkingService, billingService)
    }

    @Test
    fun shouldAskGarageLoggingServiceToFindActiveGarageLoggingByLicensePlateWhenLoggingExit() {
        val licensePlate = "ZUL0002"
        val carExit = buildSomeCarExit(licensePlate = licensePlate)
        `when`(garageLoggingService.findActiveGarageLoggingBy(licensePlate = eq(licensePlate)))
            .thenReturn(Mono.empty())

        StepVerifier.create(service.logExit(carExit = carExit))
            .verifyError()

        verify(garageLoggingService, atLeastOnce()).findActiveGarageLoggingBy(licensePlate = eq(licensePlate))
    }

    @Test
    fun shouldThrowNotFoundGarageLoggingExceptionWhenCantFindAnyActiveGarageLoggingByLicensePlateOnLoggingExit() {
        val licensePlate = "ZUL0002"
        val carExit = buildSomeCarExit(licensePlate = licensePlate)
        `when`(garageLoggingService.findActiveGarageLoggingBy(licensePlate = eq(licensePlate)))
            .thenReturn(Mono.empty())

        StepVerifier.create(service.logExit(carExit = carExit))
            .consumeErrorWith { error ->
                assertThat(error,allOf(
                    instanceOf(NotFoundGarageLoggingException::class.java),
                    hasProperty("No active car logging was found for license plate: '${licensePlate}'")
                ))
            }
    }

    @Test
    fun shouldAskGarageLoggingServiceToLogExitByCarExitWhenFindSomeActiveGarageLoggingOnLoggingExit() {
        val licensePlate = "ZUL0002"
        val carExit = buildSomeCarExit(licensePlate = licensePlate)
        val garageLogging = buildSomeGarageLogging()
        val parking = buildSomeParking()
        val billing = buildSomeBilling(garageLogging = garageLogging)
        `when`(garageLoggingService.findActiveGarageLoggingBy(licensePlate = eq(licensePlate)))
            .thenReturn(Mono.just(garageLogging))
        `when`(garageLoggingService.logExit(garageLogging = eq(garageLogging), carExit = eq(carExit)))
            .thenReturn(Mono.just(garageLogging))
        `when`(parkingService.findEnteredBy(licensePlate = eq(licensePlate)))
            .thenReturn(Mono.just(parking))
        `when`(parkingService.unparkCarFromSpot(parking = eq(parking)))
            .thenReturn(Mono.just(parking))
        `when`(billingService.chargeParking(garageLogging = eq(garageLogging), parking = eq(parking)))
            .thenReturn(Mono.just(billing))

        StepVerifier.create(service.logExit(carExit = carExit)).verifyComplete()

        verify(garageLoggingService, atLeastOnce()).logExit(
            garageLogging = eq(garageLogging),
            carExit = eq(carExit))
    }

}