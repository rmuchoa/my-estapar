package com.estapar.domain.revenue

import com.estapar.domain.car.billing.BillingService
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.LocalDateTime

open class RevenueReportService(
    val billingService: BillingService
) {

    fun mountRevenueReportFor(filter: RevenueFilter): Mono<RevenueReport> =
        billingService.searchBillingsBy(filter)
            .map { billing -> billing.chargedAmount }
            .reduce(BigDecimal.ZERO) { accumulator, chargedAmount ->
                accumulator.plus(chargedAmount)
            }
                .map { totalAmount ->
                    RevenueReport(
                        amount = totalAmount,
                        currency = CurrencyCode.BRL,
                        timestamp = LocalDateTime.now())
                }

}