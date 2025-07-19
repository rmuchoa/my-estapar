package com.estapar.domain.car.billing

import com.estapar.domain.car.logging.GarageLogging
import com.estapar.domain.car.park.Parking
import com.estapar.domain.garage.sector.Sector
import com.estapar.domain.garage.spot.Spot
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime

@ExtendWith(MockitoExtension::class)
class BillingServiceTest(@Mock private val repository: BillingRepository) {

    private lateinit var service: BillingService

    @BeforeEach
    fun setUp() {
        service = BillingService(repository)
    }

    @Test
    fun shouldAskRepositoryToSaveBillingWhenChargingParking() {
        val sector = Sector(id = 1, name = "A", basePrice = BigDecimal.TEN, maxCapacity = 20,
            openHour = LocalTime.of(9,0), closedHour = LocalTime.of(20,0), durationLimitMinutes = 30)
        val spot = Spot(id = 2, sector = sector, latitude = 10.234, longitude = 32.451, occupied = false)
        val garageLogging = GarageLogging(id = 3, licensePlate = "ZUL0001", entryTime = LocalDateTime.now())
        val parking = Parking(id = 4, spot = spot, licensePlate = "ZUL0001")

        service.chargeParking(garageLogging, parking)
    }

}