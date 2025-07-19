package com.estapar

import com.estapar.domain.car.billing.Billing
import com.estapar.domain.car.logging.GarageLogging
import com.estapar.domain.car.logging.entry.CarEntry
import com.estapar.domain.car.logging.exit.CarExit
import com.estapar.domain.car.park.Parking
import com.estapar.domain.garage.sector.Sector
import com.estapar.domain.garage.spot.Spot
import com.estapar.domain.revenue.RevenueBilling
import com.estapar.domain.revenue.RevenueFilter
import com.estapar.infrastructure.client.dto.GarageSectorDTO
import com.estapar.infrastructure.client.dto.LoadGarageResponse
import com.estapar.infrastructure.client.dto.SpotDTO
import com.estapar.infrastructure.repository.postgresql.entity.GarageSectorEntity
import com.estapar.infrastructure.repository.postgresql.entity.GarageSpotEntity
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

const val LICENSE_PLATE: String = "ZUL0001"
const val SECTOR_NAME: String = "A"

abstract class AbstractEstaparTest {

    fun buildSomeSector(
        id: Long = 1,
        name: String = SECTOR_NAME,
        basePrice: BigDecimal = BigDecimal.TEN,
        maxCapacity: Int = 20,
        durationLimitMinutes: Int = 30,
        openHour: LocalTime = LocalTime.of(9,0),
        closedHour: LocalTime = LocalTime.of(20,0)
    ): Sector =
        Sector(
            id = id,
            name = name,
            basePrice = basePrice,
            maxCapacity = maxCapacity,
            durationLimitMinutes = durationLimitMinutes,
            openHour = openHour,
            closedHour = closedHour)

    fun buildSomeSpot(
        id: Long = 2,
        sector: Sector = buildSomeSector(),
        latitude: Double = 20.14,
        longitude: Double = 15.24,
        occupied: Boolean = true
    ): Spot =
        Spot(
            id = id,
            sector = sector,
            latitude = latitude,
            longitude = longitude,
            occupied = occupied)

    fun buildSomeParking(
        id: Long = 3,
        spot: Spot = buildSomeSpot(),
        licensePlate: String = LICENSE_PLATE
    ): Parking =
        Parking(
            id = id,
            spot = spot,
            licensePlate = licensePlate)

    fun buildSomeGarageLogging(
        id: Long = 4,
        licensePlate: String = LICENSE_PLATE,
        entryTime: LocalDateTime = LocalDateTime.now(),
        exitTime: LocalDateTime = LocalDateTime.now()
    ): GarageLogging =
        GarageLogging(
            id = id,
            licensePlate = licensePlate,
            entryTime = entryTime,
            exitTime = exitTime)

    fun buildGarageLoggingWithTimeRangeInMinutes(
        minutes: Long,
        licensePlate: String = LICENSE_PLATE
    ): GarageLogging =
        buildSomeGarageLogging(
            entryTime = LocalDateTime.now().minusMinutes(minutes),
            exitTime = LocalDateTime.now(),
            licensePlate = licensePlate)

    fun buildGarageLoggingWithTimeRangeInHours(
        hours: Long,
        licensePlate: String = LICENSE_PLATE
    ): GarageLogging =
        buildSomeGarageLogging(
            entryTime = LocalDateTime.now().minusHours(hours),
            exitTime = LocalDateTime.now(),
            licensePlate = licensePlate)

    fun buildSomeCarEntry(
        licensePlate: String = LICENSE_PLATE,
        entryTime: LocalDateTime = LocalDateTime.now()
    ): CarEntry =
        CarEntry(
            licensePlate = licensePlate,
            entryTime = entryTime)

    fun buildSomeCarExit(
        licensePlate: String = LICENSE_PLATE,
        exitTime: LocalDateTime = LocalDateTime.now()
    ): CarExit =
        CarExit(
            licensePlate = licensePlate,
            exitTime = exitTime)

    fun buildSomeBilling(
        id: Long = 5,
        parking: Parking = buildSomeParking(),
        garageLogging: GarageLogging = buildSomeGarageLogging(),
        licensePlate: String = LICENSE_PLATE,
        billingTime: LocalDateTime = LocalDateTime.now(),
        billingDuration: Duration = 10.toDuration(DurationUnit.SECONDS),
        chargedAmount: BigDecimal = BigDecimal.valueOf(25.0)
    ): Billing =
        Billing(
            id = id,
            parking = parking,
            garageLogging = garageLogging,
            licensePlate = licensePlate,
            billingTime = billingTime,
            billingDuration = billingDuration,
            chargedAmount = chargedAmount)

    fun buildSomeRevenueBilling(
        licensePlate: String = LICENSE_PLATE,
        billingTime: LocalDateTime = LocalDateTime.now(),
        billingDuration: Duration = 10.toDuration(DurationUnit.SECONDS),
        chargedAmount: BigDecimal = BigDecimal.valueOf(25.0)
    ): RevenueBilling =
        RevenueBilling(
            licensePlate = licensePlate,
            billingTime = billingTime,
            billingDuration = billingDuration,
            chargedAmount = chargedAmount)

    fun buildSomeRevenueFilter(
        date: LocalDate = LocalDate.of(2025, 7, 20),
        sectorName: String = SECTOR_NAME
    ): RevenueFilter =
        RevenueFilter(
            date = date,
            sectorName = sectorName)

    fun buildSomeGarageSectorDTO(
        sector: String = "A",
        maxCapacity: Int = 100,
        basePrice: BigDecimal = BigDecimal.valueOf(30.24),
        openHour: LocalTime = LocalTime.of(9, 10),
        closeHour: LocalTime = LocalTime.of(15, 45),
        durationLimitMinutes: Int = 20
    ): GarageSectorDTO =
        GarageSectorDTO(
            sector = sector,
            basePrice = basePrice,
            maxCapacity = maxCapacity,
            openHour = openHour,
            closeHour = closeHour,
            durationLimitMinutes = durationLimitMinutes)

    fun buildSomeSpotDTO(
        id: Long = 1,
        sector: String = "A",
        latitude: Double = 20.34,
        longitude: Double = 45.29,
        occupied: Boolean = true
    ): SpotDTO =
        SpotDTO(
            id = id,
            sector = sector,
            latitude = latitude,
            longitude = longitude,
            occupied = occupied)

    fun buildSomeGarageResponse(
        sector: GarageSectorDTO,
        spot: SpotDTO
    ): LoadGarageResponse =
        LoadGarageResponse(
            garage = listOf(sector),
            spots = listOf(spot))

    fun buildSomeGarageSectorEntity(
        id: Long = 1,
        sector: Sector = buildSomeSector()
    ): GarageSectorEntity =
        GarageSectorEntity(
            id = id,
            name = sector.name,
            basePrice = sector.basePrice,
            maxCapacity = sector.maxCapacity,
            durationLimitMinutes = sector.durationLimitMinutes,
            openHour = sector.openHour,
            closedHour = sector.closedHour
        )

    fun buildSomeGarageSpotEntity(
        id: Long = 2,
        spot: Spot = buildSomeSpot()
    ): GarageSpotEntity =
        GarageSpotEntity(
            id = id,
            sectorId = spot.sector!!.id,
            latitude = spot.latitude,
            longitude = spot.longitude,
            occupied = spot.occupied
        )

}