package com.estapar.domain.garage.spot

import com.estapar.domain.park.DynamicPriceRule
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

    fun hasReachedSectorDurationLimit(duration: Duration): Boolean =
        sector?.hasReachedDurationLimit(duration)?: false

    fun hasReachedSectorMaxCapacity(): Boolean =
        sector?.hasReachedMaxCapacity()?: false

    fun definePriceRuleByCapacity(): DynamicPriceRule =
        sector?.definePriceRuleByCapacity()?: DynamicPriceRule.TWENTY_FIVE_PERCENT_CAPACITY

}