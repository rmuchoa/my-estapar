package com.estapar.infrastructure.component

import com.estapar.domain.car.billing.BillingRepository
import com.estapar.domain.car.billing.BillingService
import org.springframework.stereotype.Service

@Service
class BillingServiceImpl(
    repository: BillingRepository
) : BillingService(repository)