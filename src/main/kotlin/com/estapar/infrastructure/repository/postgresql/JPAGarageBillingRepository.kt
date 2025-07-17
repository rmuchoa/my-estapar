package com.estapar.infrastructure.repository.postgresql

import com.estapar.infrastructure.repository.postgresql.entity.GarageBillingEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import java.time.LocalDate

interface JPAGarageBillingRepository :
    ReactiveCrudRepository<GarageBillingEntity, Long> {

    @Query(
        """
    SELECT 
      b.id,
      b.logging_id,
      b.parking_id,
      b.license_plate,
      b.billing_time,
      b.billing_duration_time,
      b.charged_amount
    FROM garage_billing b
    INNER JOIN garage_parking p ON p.id = b.parking_id
    INNER JOIN garage_spot s    ON s.id = p.spot_id
    INNER JOIN garage_sector sec ON sec.id = s.sector_id
    WHERE sec.name = :sectorName
      AND b.billing_time BETWEEN :startDate AND :endDate
    """
    )
    fun findAllBySectorNameAndBillingTimeBetween(
        sectorName: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flux<GarageBillingEntity>

}