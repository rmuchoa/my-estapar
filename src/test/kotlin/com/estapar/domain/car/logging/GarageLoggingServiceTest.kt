package com.estapar.domain.car.logging

import com.estapar.AbstractEstaparTest
import com.estapar.domain.car.GarageLoggingStatus
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

@ExtendWith(MockitoExtension::class)
class GarageLoggingServiceTest(
    @Mock private val repository: GarageLoggingRepository
) : AbstractEstaparTest() {

    private val garageLoggingCaptor = argumentCaptor<GarageLogging>()
    private lateinit var service: GarageLoggingService

    @BeforeEach
    fun setUp() {
        service = GarageLoggingService(repository)
    }

    @Test
    fun shouldAskGarageLoggingRepositoryToSaveGarageLoggingBasedOCarEntryWhenLoggingEntry() {
        val carEntry = buildSomeCarEntry()
        `when`(repository.saveGarageLogging(garageLogging = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.logEntry(carEntry = carEntry)).verifyComplete()

        verify(repository, atLeastOnce()).saveGarageLogging(garageLogging = garageLoggingCaptor.capture())

        assertThat(garageLoggingCaptor.firstValue,allOf(
            instanceOf(GarageLogging::class.java),
            hasProperty("id", nullValue()),
            hasProperty("licensePlate", equalTo(carEntry.licensePlate)),
            hasProperty("entryTime", equalTo(carEntry.entryTime)),
            hasProperty("status", equalTo(GarageLoggingStatus.ACTIVE)),
            hasProperty("exitTime", nullValue())
        ))
    }

    @Test
    fun shouldReturnSavedGarageLoggingWhenLoggingEntry() {
        val carEntry = buildSomeCarEntry()
        val garageLogging = buildSomeGarageLogging()
        `when`(repository.saveGarageLogging(garageLogging = any())).thenReturn(Mono.just(garageLogging))

        StepVerifier.create(service.logEntry(carEntry = carEntry))
            .assertNext { garageLogging ->
                assertThat(garageLogging,allOf(
                    instanceOf(GarageLogging::class.java),
                    hasProperty("id", equalTo(garageLogging.id)),
                    hasProperty("licensePlate", equalTo(garageLogging.licensePlate)),
                    hasProperty("entryTime", equalTo(garageLogging.entryTime)),
                    hasProperty("exitTime", equalTo(garageLogging.exitTime)),
                    hasProperty("status", equalTo(garageLogging.status))
                ))
            }
            .verifyComplete()


    }

    @Test
    fun shouldAskGarageLoggingRepositoryToSaveGarageLoggingBasedOCarExitWhenLoggingExit() {
        val licensePlate = "ZUL0003"
        val carExit = buildSomeCarExit()
        val garageLogging = buildSomeGarageLogging(licensePlate = licensePlate)
        `when`(repository.saveGarageLogging(garageLogging = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.logExit(garageLogging = garageLogging, carExit = carExit)).verifyComplete()

        verify(repository, atLeastOnce()).saveGarageLogging(garageLogging = garageLoggingCaptor.capture())

        assertThat(garageLoggingCaptor.firstValue,allOf(
            instanceOf(GarageLogging::class.java),
            hasProperty("id", equalTo(garageLogging.id)),
            hasProperty("licensePlate", equalTo(licensePlate)),
            hasProperty("entryTime", equalTo(garageLogging.entryTime)),
            hasProperty("status", equalTo(GarageLoggingStatus.CLOSED)),
            hasProperty("exitTime", equalTo(carExit.exitTime))
        ))
    }

    @Test
    fun shouldReturnSavedGarageLoggingWhenLoggingExit() {
        val carExit = buildSomeCarExit()
        val garageLogging = buildSomeGarageLogging()
        `when`(repository.saveGarageLogging(garageLogging = any())).thenReturn(Mono.just(garageLogging))

        StepVerifier.create(service.logExit(garageLogging = garageLogging, carExit = carExit))
            .assertNext { garageLogging ->
                assertThat(garageLogging,allOf(
                    instanceOf(GarageLogging::class.java),
                    hasProperty("id", equalTo(garageLogging.id)),
                    hasProperty("licensePlate", equalTo(garageLogging.licensePlate)),
                    hasProperty("entryTime", equalTo(garageLogging.entryTime)),
                    hasProperty("exitTime", equalTo(garageLogging.exitTime)),
                    hasProperty("status", equalTo(garageLogging.status))
                ))
            }
            .verifyComplete()


    }

    @Test
    fun shouldAskGarageLoggingRepositoryToFindActiveByLicensePlateWhenFindingActiveByLicensePlate() {
        val licensePlate = "ZUL0003"
        `when`(repository.findActiveByLicensePlate(licensePlate = eq(licensePlate))).thenReturn(Mono.empty())

        StepVerifier.create(service.findActiveGarageLoggingBy(licensePlate = licensePlate)).verifyComplete()

        verify(repository, atLeastOnce()).findActiveByLicensePlate(licensePlate = eq(licensePlate))
    }

    @Test
    fun shouldReturnGarageLoggingWhenFindSomeActiveByLicensePlate() {
        val licensePlate = "ZUL0003"
        val garageLogging = buildSomeGarageLogging()
        `when`(repository.findActiveByLicensePlate(licensePlate = eq(licensePlate)))
            .thenReturn(Mono.just(garageLogging))

        StepVerifier.create(service.findActiveGarageLoggingBy(licensePlate = licensePlate))
            .assertNext { garageLogging ->
                assertThat(garageLogging,allOf(
                    instanceOf(GarageLogging::class.java),
                    hasProperty("id", equalTo(garageLogging.id)),
                    hasProperty("licensePlate", equalTo(garageLogging.licensePlate)),
                    hasProperty("entryTime", equalTo(garageLogging.entryTime)),
                    hasProperty("exitTime", equalTo(garageLogging.exitTime)),
                    hasProperty("status", equalTo(garageLogging.status))
                ))
            }
            .verifyComplete()
    }

}