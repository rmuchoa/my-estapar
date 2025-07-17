package com.estapar.domain.revenue

import java.math.BigDecimal
import java.time.LocalDateTime

data class RevenueReport(
    val amount: BigDecimal,
    val currency: CurrencyCode,
    val timestamp: LocalDateTime
)
