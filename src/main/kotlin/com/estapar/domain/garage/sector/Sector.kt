package com.estapar.domain.garage.sector

import com.estapar.domain.car.park.DynamicPriceRule
import com.estapar.domain.garage.spot.Spot
import java.math.BigDecimal
import java.time.LocalTime
import kotlin.math.ceil
import kotlin.time.Duration

const val ONE_HOUR_MINUTES = 60
const val A_QUARTER_OF_AN_HOUR = 15.0

data class Sector(
    val id: Long? = null,
    val name: String,
    val basePrice: BigDecimal,
    val maxCapacity: Int,
    val openHour: LocalTime,
    val closeHour: LocalTime,
    val durationLimitMinutes: Int,
    val status: SectorStatus = SectorStatus.OPENED,
    val spots: List<Spot> = emptyList()) {

    fun getAllOccupiedSpots(): List<Spot> =
        spots.filter { spot -> spot.occupied }

    fun isClosedFor(parkingTime: LocalTime): Boolean =
        parkingTime.isBefore(openHour) && parkingTime.isAfter(closeHour)

    fun hasReachedDurationLimit(duration: Duration): Boolean =
        duration.inWholeMinutes > durationLimitMinutes

    fun hasReachedMaxCapacity(): Boolean =
        getAllOccupiedSpots().size >= maxCapacity

    fun definePriceRuleByCapacity(): DynamicPriceRule =
        when((getOccupiedSpotsNumberPlusOne() * 100) / maxCapacity) {
            in 75.0..100.0 -> DynamicPriceRule.ONE_HUNDRED_PERCENT_CAPACITY
            in 50.0..75.0 -> DynamicPriceRule.SEVENTY_FIVE_PERCENT_CAPACITY
            in 25.0..50.0 -> DynamicPriceRule.FIFTY_PERCENT_CAPACITY
            in 0.0..25.0 -> DynamicPriceRule.TWENTY_FIVE_PERCENT_CAPACITY
            else -> DynamicPriceRule.TWENTY_FIVE_PERCENT_CAPACITY
        }

    private fun getOccupiedSpotsNumberPlusOne(): Double =
        getAllOccupiedSpots().size.toDouble() + 1.0

    fun generateChargeFor(billingDuration: Duration, priceRule: DynamicPriceRule): BigDecimal =
        when (billingDuration.inWholeMinutes) {
            in 0..15 -> BigDecimal.ZERO
            in 15..60 -> basePrice
            else -> {
                val fourParts = BigDecimal.valueOf( 4.0)
                val discountedPricePerHour = priceRule.applyPriceRatePercentageOn(basePrice)
                val discountedPricePer15Minutes = discountedPricePerHour.divide(fourParts)
                val excessMinutes = billingDuration.inWholeMinutes - ONE_HOUR_MINUTES
                val numberBlocksOf15Minutes = ceil(excessMinutes / A_QUARTER_OF_AN_HOUR)
                basePrice + (discountedPricePer15Minutes * numberBlocksOf15Minutes.toBigDecimal())
            }
        }

}
