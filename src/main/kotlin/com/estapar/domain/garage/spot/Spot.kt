package com.estapar.domain.garage.spot

import com.estapar.domain.car.park.DynamicPriceRule
import com.estapar.domain.garage.sector.Sector
import java.math.BigDecimal
import java.time.LocalTime
import kotlin.time.Duration

data class Spot(
    val id: Long? = null,
    val sector: Sector?,
    val latitude: Double,
    val longitude: Double,
    val occupied: Boolean) {

    fun isStillOccupied(): Boolean =
        occupied

    fun isSectorClosedFor(parkingTime: LocalTime): Boolean =
        sector!!.isClosedFor(parkingTime)

    fun hasReachedSectorDurationLimit(duration: Duration): Boolean =
        sector!!.hasReachedDurationLimit(duration)

    fun hasReachedSectorMaxCapacity(): Boolean =
        sector!!.hasReachedMaxCapacity()

    fun definePriceRuleByCapacity(): DynamicPriceRule =
        sector!!.definePriceRuleByCapacity()

    fun generateChargeFor(billingDuration: Duration, priceRule: DynamicPriceRule): BigDecimal =
        sector!!.generateChargeFor(billingDuration, priceRule)

}