package com.estapar.infrastructure.repository.postgresql.entity

import com.estapar.domain.car.billing.Billing
import com.estapar.domain.car.logging.GarageLogging
import com.estapar.domain.car.park.Parking
import com.estapar.domain.revenue.RevenueBilling
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.LocalTime.MIDNIGHT
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

@Table(name = "garage_billing")
data class GarageBillingEntity(
    @Id @Column("id") val id: Long? = null,
    @Column("logging_id") val loggingId: Long,
    @Column("parking_id") val parkingId: Long,
    @Column("license_plate") val licensePlate: String,
    @Column("billing_time") val billingTime: LocalDateTime,
    @Column("billing_duration_time") val billingDurationTime: LocalTime,
    @Column("charged_amount") val chargedAmount: BigDecimal
) {

    fun toDomain(
        garageLogging: GarageLogging,
        parking: Parking): Billing =
        Billing(
            id = id,
            parking = parking,
            garageLogging = garageLogging,
            licensePlate = licensePlate,
            billingTime = billingTime,
            billingDuration = getBillingDuration(),
            chargedAmount = chargedAmount)

    fun toRevenueDomain(): RevenueBilling =
        RevenueBilling(
            licensePlate = licensePlate,
            billingTime = billingTime,
            billingDuration = getBillingDuration(),
            chargedAmount = chargedAmount)

    companion object {
        fun of(billing: Billing): GarageBillingEntity =
            GarageBillingEntity(
                id = billing.id,
                parkingId = billing.parking.id!!,
                loggingId = billing.garageLogging.id!!,
                licensePlate = billing.licensePlate,
                chargedAmount = billing.chargedAmount,
                billingTime = billing.billingTime,
                billingDurationTime = billing.billingDuration.inWholeSeconds
                    .let { secondOfDay -> LocalTime.ofSecondOfDay(secondOfDay) })
    }

    private fun getBillingDuration(): Duration =
        java.time.Duration.between(MIDNIGHT, billingDurationTime)
            .toKotlinDuration()

}
