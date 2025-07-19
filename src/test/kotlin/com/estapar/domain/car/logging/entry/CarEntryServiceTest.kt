package com.estapar.domain.car.logging.entry

import com.estapar.AbstractEstaparTest
import com.estapar.domain.car.logging.AlreadyHasActiveGarageLoggingException
import com.estapar.domain.car.logging.GarageLoggingService
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
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class CarEntryServiceTest(
    @Mock private val garageLoggingService: GarageLoggingService
) : AbstractEstaparTest() {

    private lateinit var service: CarEntryService

    @BeforeEach
    fun setUp() {
        service = CarEntryService(garageLoggingService)
    }

    @Test
    fun shouldAskGarageLoggingServiceToFindActiveGarageLoggingByLicensePlateWhenLoggingEntry() {
        val licensePlate = "ZUL0002"
        val carEntry = buildSomeCarEntry(licensePlate = licensePlate)
        `when`(garageLoggingService.findActiveGarageLoggingBy(licensePlate = eq(licensePlate))).thenReturn(Mono.empty())
        `when`(garageLoggingService.logEntry(carEntry = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.logEntry(carEntry = carEntry)).verifyComplete()

        verify(garageLoggingService, atLeastOnce()).findActiveGarageLoggingBy(licensePlate = eq(carEntry.licensePlate))
    }

    @Test
    fun shouldThrowAlreadyHasActiveGarageLoggingExceptionWhenLoggingEntryForAlreadyExistentGarageLogging() {
        val licensePlate = "ZUL0002"
        val carEntry = buildSomeCarEntry(licensePlate = licensePlate)
        val garageLogging = buildSomeGarageLogging(licensePlate = licensePlate)
        `when`(garageLoggingService.findActiveGarageLoggingBy(licensePlate = eq(licensePlate)))
            .thenReturn(Mono.just(garageLogging))
        `when`(garageLoggingService.logEntry(carEntry = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.logEntry(carEntry = carEntry))
            .consumeErrorWith { error ->
                assertThat(error,allOf(
                    instanceOf(AlreadyHasActiveGarageLoggingException::class.java),
                    hasProperty("message", equalTo("License plate '${licensePlate}' has already logged in garage recently!"))
                ))
            }
    }

    @Test
    fun shouldAskGarageLoggingServiceToLogEntryByCarEntryWhenCantFindAnyActiveGarageLoggingOnLoggingEntry() {
        val carEntry = buildSomeCarEntry()
        `when`(garageLoggingService.findActiveGarageLoggingBy(licensePlate = eq(carEntry.licensePlate)))
            .thenReturn(Mono.empty())
        `when`(garageLoggingService.logEntry(carEntry = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.logEntry(carEntry = carEntry)).verifyComplete()

        verify(garageLoggingService, atLeastOnce()).logEntry(carEntry = eq(carEntry))
    }

}