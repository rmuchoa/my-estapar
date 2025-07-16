package com.estapar.domain.car.billing

import com.estapar.domain.car.logging.GarageLogging
import com.estapar.domain.car.park.Parking
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.time.Duration

data class Billing(
    val id: Long? = null,
    val parking: Parking,
    val garageLogging: GarageLogging,
    val licensePlate: String,
    val billingTime: LocalDateTime = LocalDateTime.now(),
    val billingDuration: Duration,
    val chargedAmount: BigDecimal
) {

    companion object {
        fun of(garageLogging: GarageLogging, parking: Parking): Billing =
            garageLogging.getBillingDuration()
                .let { billingDuration ->
                    Billing(
                        parking = parking,
                        garageLogging = garageLogging,
                        licensePlate = garageLogging.licensePlate,
                        chargedAmount = parking.generateChargeFor(billingDuration),
                        billingDuration = billingDuration)
                }
    }

}
