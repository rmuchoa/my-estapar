package com.estapar.infrastructure.repository

import com.estapar.domain.car.billing.Billing
import com.estapar.domain.car.billing.BillingRepository
import com.estapar.domain.revenue.RevenueBilling
import com.estapar.domain.revenue.RevenueFilter
import com.estapar.infrastructure.repository.postgresql.JPAGarageBillingRepository
import com.estapar.infrastructure.repository.postgresql.entity.GarageBillingEntity
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class BillingRepositoryAdapter(
    val repository: JPAGarageBillingRepository
) : BillingRepository {

    override fun saveBilling(billing: Billing): Mono<Billing> =
        repository.save<GarageBillingEntity>(GarageBillingEntity.of(billing))
            .map { entity -> entity.toDomain(
                parking = billing.parking,
                garageLogging = billing.garageLogging)
            }

    override fun searchBillingsBy(filter: RevenueFilter): Flux<RevenueBilling> =
        repository.findAllBySectorNameAndBillingTimeBetween(
            sectorName = filter.sectorName,
            startDate = filter.date,
            endDate = filter.date.plusDays(1))
            .map { entity -> entity.toRevenueDomain() }

}