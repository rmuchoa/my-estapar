package com.estapar.domain.car.logging

import com.estapar.AbstractEstaparTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@ExtendWith(MockitoExtension::class)
class GarageLoggingTest : AbstractEstaparTest() {

    lateinit var logging: GarageLogging

    @BeforeEach
    fun setUp() {
        logging = buildSomeGarageLogging(
            entryTime = LocalDateTime.now().minusHours(1),
            exitTime = LocalDateTime.now().minusMinutes(45))
    }

    @Test
    fun shouldReturnDurationBetweenEntryTimeAndExitTimeWhenGetBillingDuration() {
        val duration = logging.getBillingDuration()

        assertEquals(actual = duration, expected = 15.toDuration(DurationUnit.MINUTES), message = "Wasn't equal")
    }

    @Test
    fun shouldReturnDurationBetweenEntryTimeAndRightNowWhenGetUntilNowDuration() {
        val duration = logging.getUntilNowDuration()

        assertEquals(actual = duration.inWholeHours, expected = 60.toDuration(DurationUnit.MINUTES).inWholeHours, message = "Wasn't equal")
    }

}