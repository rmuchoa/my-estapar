package com.estapar.infrastructure.api

import com.estapar.domain.revenue.RevenueReportService
import com.estapar.domain.revenue.RevenueFilter
import com.estapar.infrastructure.api.dto.response.RevenueReportResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.time.LocalDate

@RestController
@RequestMapping("/revenue")
class RevenueReportController(
    val revenueReportService: RevenueReportService
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping()
    fun mountRevenueReportForLicensePlate(
        @RequestParam("date") date: LocalDate,
        @RequestParam("sector") sectorName: String
    ): Mono<RevenueReportResponse> =
        revenueReportService.mountRevenueReportFor(
            RevenueFilter.of(date = date, sectorName = sectorName))
            .map { revenueReport ->
                RevenueReportResponse(
                    amount = revenueReport.amount,
                    currency = revenueReport.currency.name,
                    timestamp = revenueReport.timestamp)
            }

}