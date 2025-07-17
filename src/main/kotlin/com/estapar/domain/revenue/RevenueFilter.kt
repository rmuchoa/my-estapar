package com.estapar.domain.revenue

import java.time.LocalDate

data class RevenueFilter(
    val date: LocalDate,
    val sectorName: String
) {
    companion object {
        fun of(date: LocalDate, sectorName: String): RevenueFilter =
            RevenueFilter(
                date = date,
                sectorName = sectorName)
    }
}