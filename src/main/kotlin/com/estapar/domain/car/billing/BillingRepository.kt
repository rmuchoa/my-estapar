package com.estapar.domain.car.billing

import reactor.core.publisher.Mono

interface BillingRepository {
    fun saveBilling(billing: Billing): Mono<Billing>
}