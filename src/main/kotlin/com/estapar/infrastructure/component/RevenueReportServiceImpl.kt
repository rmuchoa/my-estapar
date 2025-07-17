package com.estapar.infrastructure.component

import com.estapar.domain.car.billing.BillingService
import com.estapar.domain.revenue.RevenueReportService
import org.springframework.stereotype.Service

@Service
class RevenueReportServiceImpl(
    billingService: BillingService
) : RevenueReportService(billingService)