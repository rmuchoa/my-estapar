package com.estapar.domain.garage.spot

import com.estapar.domain.garage.park.DynamicPriceRule
import com.estapar.domain.garage.sector.Sector
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
        sector?.isClosedFor(parkingTime)?: false

    fun hasOverstayAlert(duration: Duration): Boolean =
        sector?.hasOverDurationAlert(duration)?: false

    fun hasOvercapacityAlert(): Boolean =
        sector?.hasOverCapacityAlert()?: false

    fun definePriceRuleByCapacity(): DynamicPriceRule =
        sector?.definePriceRuleByCapacity()?: DynamicPriceRule.TWENTY_FIVE_PERCENT_CAPACITY

}