package com.estapar.domain.car.billing

import com.estapar.AbstractEstaparTest
import com.estapar.SECTOR_NAME
import com.estapar.domain.revenue.RevenueBilling
import com.estapar.domain.revenue.RevenueFilter
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
import org.mockito.kotlin.argumentCaptor
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.test.DefaultAsserter.assertEquals
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@ExtendWith(MockitoExtension::class)
class BillingServiceTest(
    @Mock private val repository: BillingRepository
) : AbstractEstaparTest() {

    private val billingCaptor = argumentCaptor<Billing>()
    private val filterCaptor = argumentCaptor<RevenueFilter>()
    private lateinit var service: BillingService

    @BeforeEach
    fun setUp() {
        service = BillingService(repository)
    }

    @Test
        fun shouldAskRepositoryToSaveBillingWhenChargeParking() {
        val parking = buildSomeParking()
        val garageLogging = buildSomeGarageLogging()
        `when`(repository.saveBilling(billing = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.chargeParking(garageLogging = garageLogging, parking = parking))
            .verifyComplete()

        verify(repository, atLeastOnce()).saveBilling(billing = any())
    }

    @Test
    fun shouldSaveBillingWithZeroChargeAmountWhenChargeParkingBeforeFifteenMinutes() {
        val licensePlate = "ZUL0002"
        val parking = buildSomeParking()
        val garageLogging = buildGarageLoggingWithTimeRangeInMinutes(minutes = 12, licensePlate = licensePlate)
        `when`(repository.saveBilling(billing = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.chargeParking(garageLogging = garageLogging, parking = parking))
            .verifyComplete()

        verify(repository, atLeastOnce()).saveBilling(billing = billingCaptor.capture())

        assertThat(billingCaptor.firstValue,allOf(
            instanceOf(Billing::class.java),
            hasProperty("parking", equalTo(parking)),
            hasProperty("garageLogging", equalTo(garageLogging)),
            hasProperty("licensePlate", equalTo(licensePlate)),
            hasProperty("billingTime", instanceOf<LocalDateTime>(LocalDateTime::class.java)),
            hasProperty("chargedAmount", equalTo(BigDecimal.ZERO))
        ))

        assertEquals(actual = billingCaptor.firstValue.billingDuration.inWholeMinutes, expected = 12.toDuration(DurationUnit.MINUTES).inWholeMinutes, message = "Wasn't equal")
    }

    @Test
    fun shouldSaveBillingWithChargeAmountFromSectorBasePriceWhenChargeParkingAfterFifteenMinutesButBeforeOneHour() {
        val sector = buildSomeSector(basePrice = BigDecimal.TEN)
        val parking = buildSomeParking(spot = buildSomeSpot(sector = sector))
        val garageLogging = buildGarageLoggingWithTimeRangeInMinutes(minutes = 20)
        `when`(repository.saveBilling(billing = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.chargeParking(garageLogging = garageLogging, parking = parking))
            .verifyComplete()

        verify(repository, atLeastOnce()).saveBilling(billing = billingCaptor.capture())

        assertThat(billingCaptor.firstValue,allOf(
            instanceOf(Billing::class.java),
            hasProperty("parking", equalTo(parking)),
            hasProperty("garageLogging", equalTo(garageLogging)),
            hasProperty("licensePlate", equalTo(garageLogging.licensePlate)),
            hasProperty("billingTime", instanceOf<LocalDateTime>(LocalDateTime::class.java)),
            hasProperty("chargedAmount", equalTo(sector.basePrice))
        ))

        assertEquals(actual = billingCaptor.firstValue.billingDuration.inWholeMinutes, expected = 20.toDuration(DurationUnit.MINUTES).inWholeMinutes, message = "Wasn't equal")
    }

    @Test
    fun shouldSaveBillingWithChargeAmountFromSectorBasePricePlusDiscountedAmountWhenChargeParkingAfterOneHourAndPlusMoreOneHour() {
        val parking = buildSomeParking(spot = buildSomeSpot(sector = buildSomeSector(basePrice = BigDecimal.TEN)))
        val garageLogging = buildGarageLoggingWithTimeRangeInHours(hours = 2)
        `when`(repository.saveBilling(billing = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.chargeParking(garageLogging = garageLogging, parking = parking))
            .verifyComplete()

        verify(repository, atLeastOnce()).saveBilling(billing = billingCaptor.capture())

        assertThat(billingCaptor.firstValue,allOf(
            instanceOf(Billing::class.java),
            hasProperty("parking", equalTo(parking)),
            hasProperty("garageLogging", equalTo(garageLogging)),
            hasProperty("licensePlate", equalTo(garageLogging.licensePlate)),
            hasProperty("billingTime", instanceOf<LocalDateTime>(LocalDateTime::class.java)),
            hasProperty("chargedAmount", equalTo(BigDecimal.valueOf(21.0).setScale(3)))
        ))

        assertEquals(actual = billingCaptor.firstValue.billingDuration.inWholeHours, expected = 2.toDuration(DurationUnit.HOURS).inWholeHours, message = "Wasn't equal")
    }

    @Test
    fun shouldReturnSavedBillingWhenChargeParking() {
        val licensePlate = "ZUL0002"
        val billingTime = LocalDateTime.now()
        val chargedAmount = BigDecimal.TEN
        val parking = buildSomeParking()
        val garageLogging = buildSomeGarageLogging(licensePlate = licensePlate)
        val billing = buildSomeBilling(
            garageLogging = garageLogging,
            licensePlate = licensePlate,
            billingTime = billingTime,
            chargedAmount = chargedAmount)
        `when`(repository.saveBilling(billing = any())).thenReturn(Mono.just(billing))

        StepVerifier.create(service.chargeParking(garageLogging = garageLogging, parking = parking))
            .assertNext { returnedBilling ->
                assertThat(returnedBilling,allOf(
                    instanceOf(Billing::class.java),
                    hasProperty("parking", equalTo(parking)),
                    hasProperty("garageLogging", equalTo(garageLogging)),
                    hasProperty("licensePlate", equalTo(licensePlate)),
                    hasProperty("billingTime", equalTo(billingTime)),
                    hasProperty("chargedAmount", equalTo(chargedAmount))
                ))

                assertEquals(actual = returnedBilling.billingDuration.inWholeMinutes, expected = billing.billingDuration.inWholeMinutes, message = "Wasn't equal")
            }
            .verifyComplete()
    }

    @Test
    fun shouldAskRepositoryToSearchBillingsByFilterWhenSearchingBillingsByFilter() {
        val filter = buildSomeRevenueFilter()
        `when`(repository.searchBillingsBy(filter = any())).thenReturn(Flux.empty())

        StepVerifier.create(service.searchBillingsBy(filter = filter)).verifyComplete()

        verify(repository, atLeastOnce()).searchBillingsBy(filter = filterCaptor.capture())

        assertThat(filterCaptor.firstValue, allOf(
            equalTo(filter),
            instanceOf(RevenueFilter::class.java),
            hasProperty("date", equalTo(filter.date)),
            hasProperty("sectorName", equalTo(SECTOR_NAME))
        ))
    }

    @Test
    fun shouldReturnBillingWhenSearchingBillingsByFilter() {
        val filter = buildSomeRevenueFilter()
        val billing = buildSomeRevenueBilling()
        `when`(repository.searchBillingsBy(filter = any())).thenReturn(Flux.fromIterable(listOf(billing)))

        StepVerifier.create(service.searchBillingsBy(filter = filter))
            .assertNext { returnedBilling ->
                assertThat(returnedBilling,allOf(
                    instanceOf(RevenueBilling::class.java),
                    hasProperty("licensePlate", equalTo(returnedBilling.licensePlate)),
                    hasProperty("billingTime", equalTo(returnedBilling.billingTime)),
                    hasProperty("chargedAmount", equalTo(returnedBilling.chargedAmount))
                ))

                assertEquals(actual = returnedBilling.billingDuration, expected = billing.billingDuration, message = "Wasn't equal")
            }
            .verifyComplete()

    }

}