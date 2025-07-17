package com.estapar.domain.revenue

import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.time.Duration

data class RevenueBilling(
    val licensePlate: String,
    val billingTime: LocalDateTime = LocalDateTime.now(),
    val billingDuration: Duration,
    val chargedAmount: BigDecimal
)
