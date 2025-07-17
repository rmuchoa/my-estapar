package com.estapar.domain.car.billing

import com.estapar.domain.revenue.RevenueBilling
import com.estapar.domain.revenue.RevenueFilter
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface BillingRepository {
    fun saveBilling(billing: Billing): Mono<Billing>
    fun searchBillingsBy(filter: RevenueFilter): Flux<RevenueBilling>
}