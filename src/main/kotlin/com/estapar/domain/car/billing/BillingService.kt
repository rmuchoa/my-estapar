package com.estapar.domain.car.billing

import com.estapar.domain.car.logging.GarageLogging
import com.estapar.domain.car.park.Parking
import reactor.core.publisher.Mono

open class BillingService(
    val repository: BillingRepository
) {

    fun chargeParking(garageLogging: GarageLogging, parking: Parking): Mono<Billing> =
        repository.saveBilling(Billing.of(garageLogging, parking))

}