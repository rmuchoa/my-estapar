package com.estapar.infrastructure.api

import com.estapar.domain.car.park.report.ParkingReportService
import com.estapar.infrastructure.api.dto.ParkingReportResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/plate-status")
class ParkingReportController(
    val parkingReportService: ParkingReportService
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/{licensePlate}")
    fun checkParkedCarByLicensePlate(
        @PathVariable("licensePlate") licensePlate: String
    ): Mono<ParkingReportResponse> =
        parkingReportService.mountParkingReportFor(licensePlate)
            .map { parkingReport ->
                ParkingReportResponse(
                    licensePlate = parkingReport.licensePlate,
                    priceUntilNow = parkingReport.priceUntilNow,
                    entryTime = parkingReport.entryTime,
                    timeParked = parkingReport.timeParked)
            }

}