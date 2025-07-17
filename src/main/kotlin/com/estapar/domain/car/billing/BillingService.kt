package com.estapar.domain.car.billing

import com.estapar.domain.revenue.RevenueFilter
import com.estapar.domain.car.logging.GarageLogging
import com.estapar.domain.car.park.Parking
import com.estapar.domain.revenue.RevenueBilling
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

open class BillingService(
    val repository: BillingRepository
) {

    fun chargeParking(garageLogging: GarageLogging, parking: Parking): Mono<Billing> =
        repository.saveBilling(Billing.of(garageLogging, parking))

    fun searchBillingsBy(filter: RevenueFilter): Flux<RevenueBilling> =
        repository.searchBillingsBy(filter)

}