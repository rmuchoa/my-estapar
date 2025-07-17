package com.estapar.infrastructure.api

import com.estapar.domain.garage.spot.SpotReportService
import com.estapar.infrastructure.api.dto.SpotReportResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/spot-status")
class SpotReportController(
    val spotReportService: SpotReportService
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping()
    fun mountSpotReportForLatitudeAndLongitudePosition(
        @RequestParam("lat") latitude: Double,
        @RequestParam("lng") longitude: Double
    ): Mono<SpotReportResponse> =
        spotReportService.mountSpotReportFor(latitude, longitude)
            .map { spotReport ->
                SpotReportResponse(
                    ocupied = spotReport.ocupied,
                    entryTime = spotReport.entryTime,
                    timeParked = spotReport.timeParked)
            }

}