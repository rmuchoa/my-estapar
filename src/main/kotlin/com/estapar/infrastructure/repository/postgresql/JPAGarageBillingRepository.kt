package com.estapar.infrastructure.repository.postgresql

import com.estapar.infrastructure.repository.postgresql.entity.GarageBillingEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface JPAGarageBillingRepository : ReactiveCrudRepository<GarageBillingEntity, Long>