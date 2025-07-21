package com.estapar.domain.car.park.attempt

import com.estapar.AbstractEstaparTest
import com.estapar.domain.car.park.Parking
import com.estapar.domain.car.park.ParkingRepository
import com.estapar.domain.car.park.ParkingService
import com.estapar.domain.car.park.ParkingStatus
import com.estapar.domain.car.park.exception.NonOperationalGarageSectorException
import com.estapar.domain.car.park.exception.OverfilledOccupancyGarageSectorException
import com.estapar.domain.car.park.exception.UnavailableParkingSpotException
import com.estapar.domain.garage.sector.Sector
import com.estapar.domain.garage.sector.SectorService
import com.estapar.domain.garage.spot.Spot
import com.estapar.domain.garage.spot.SpotService
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.LocalDateTime
import java.time.LocalTime

@ExtendWith(MockitoExtension::class)
class ParkingServiceTest(
    @Mock private val repository: ParkingRepository,
    @Mock private val spotService: SpotService,
    @Mock private val sectorService: SectorService
) : AbstractEstaparTest() {

    private val spotCaptor = argumentCaptor<Spot>()
    private val sectorCaptor = argumentCaptor<Sector>()
    private val parkingCaptor = argumentCaptor<Parking>()
    private lateinit var service: ParkingService

    @BeforeEach
    fun setUp() {
        service = ParkingService(repository, spotService, sectorService)
    }

    @Test
    fun shouldAskParkingRepositoryFindEnteredByLicensePlateWhenFindingEnteredByLicensePlate() {
        val licensePlate = "ZUL0002"
        `when`(repository.findEnteredByLicensePlate(licensePlate = eq(licensePlate))).thenReturn(Mono.empty())

        StepVerifier.create(service.findEnteredBy(licensePlate = licensePlate)).verifyComplete()

        verify(repository, atLeastOnce()).findEnteredByLicensePlate(licensePlate = eq(licensePlate))
    }

    @Test
    fun shouldReturnSomeParkingWhenFindingEnteredByLicensePlate() {
        val licensePlate = "ZUL0002"
        val parking = buildSomeParking()
        `when`(repository.findEnteredByLicensePlate(licensePlate = eq(licensePlate)))
            .thenReturn(Mono.just(parking))

        StepVerifier.create(service.findEnteredBy(licensePlate = licensePlate))
            .assertNext { parking ->
                assertThat(parking,allOf(
                    instanceOf(Parking::class.java),
                    hasProperty("id", equalTo(parking.id)),
                    hasProperty("spot", equalTo(parking.spot)),
                    hasProperty("licensePlate", equalTo(parking.licensePlate)),
                    hasProperty("parkingTime", equalTo(parking.parkingTime)),
                    hasProperty("priceRule", equalTo(parking.priceRule)),
                    hasProperty("status", equalTo(parking.status)),
                    hasProperty("unparkingTime", nullValue())
                ))
            }
            .verifyComplete()

        verify(repository, atLeastOnce()).findEnteredByLicensePlate(licensePlate = eq(licensePlate))
    }

    @Test
    fun shouldAskParkingRepositoryFindEnteredBySpotIdWhenFindingEnteredBySpotId() {
        val spotId: Long = 4
        `when`(repository.findEnteredBySpotId(spotId = eq(spotId))).thenReturn(Mono.empty())

        StepVerifier.create(service.findEnteredBy(spotId = spotId)).verifyComplete()

        verify(repository, atLeastOnce()).findEnteredBySpotId(spotId = eq(spotId))
    }

    @Test
    fun shouldReturnSomeParkingWhenFindingEnteredBySpotId() {
        val spotId: Long = 4
        val parking: Parking = buildSomeParking()
        `when`(repository.findEnteredBySpotId(spotId = eq(spotId))).thenReturn(Mono.just(parking))

        StepVerifier.create(service.findEnteredBy(spotId = spotId))
            .assertNext { parking ->
                assertThat(parking,allOf(
                    instanceOf(Parking::class.java),
                    hasProperty("id", equalTo(parking.id)),
                    hasProperty("spot", equalTo(parking.spot)),
                    hasProperty("licensePlate", equalTo(parking.licensePlate)),
                    hasProperty("parkingTime", equalTo(parking.parkingTime)),
                    hasProperty("unparkingTime", nullValue()),
                    hasProperty("priceRule", equalTo(parking.priceRule)),
                    hasProperty("status", equalTo(parking.status))
                ))
            }
            .verifyComplete()
    }

    @Test
    fun shouldThrowUnavailableParkingSpotExceptionOnParkingCarOnSpotWhenSpotIsAlreadyOccupied() {
        val parking = buildSomeParking()

        StepVerifier.create(service.parkCarOnSpot(parking = parking))
            .consumeErrorWith { error ->
                assertThat(error,allOf(
                    instanceOf(UnavailableParkingSpotException::class.java),
                    hasProperty("message", equalTo("Parking spot still remains occupied and unavailable for parking!"))
                ))
            }
    }

    @Test
    fun shouldThrowNonOperationalGarageSectorExceptionOnParkingCarOnSpotWhenSectorIsOutOfHour() {
        val parking = buildVacantAvailableParking(openedTime = LocalTime.now().plusHours(1))

        StepVerifier.create(service.parkCarOnSpot(parking = parking))
            .consumeErrorWith { error ->
                assertThat(error,allOf(
                    instanceOf(NonOperationalGarageSectorException::class.java),
                    hasProperty("message", equalTo("Garage sector ${parking.spot.sector?.name} is out of opening hours!"))
                ))
            }
    }

    @Test
    fun shouldThrowOverfilledOccupancyGarageSectorExceptionOnParkingCarOnSpotWhenSectorHasReachedMaxCapacity() {
        val parking = buildOutOfCapacityParking()

        StepVerifier.create(service.parkCarOnSpot(parking = parking))
            .consumeErrorWith { error ->
                assertThat(error,allOf(
                    instanceOf(OverfilledOccupancyGarageSectorException::class.java),
                    hasProperty("message", equalTo("Garage sector ${parking.spot.sector?.name} has overfilled occupancy capacity!"))
                ))
            }
    }

    @Test
    fun shouldAskParkingRepositoryToSaveValidParkWhenParkingCarOnSpot() {
        val parking = buildVacantAvailableParking()
        `when`(repository.save(parking = eq(parking))).thenReturn(Mono.empty())

        StepVerifier.create(service.parkCarOnSpot(parking = parking)).verifyComplete()

        verify(repository, atLeastOnce()).save(parking = eq(parking))
    }

    @Test
    fun shouldAskSpotServiceToSaveValidParkWhenParkingCarOnSpot() {
        val parking = buildVacantAvailableParking()
        `when`(repository.save(parking = eq(parking))).thenReturn(Mono.just(parking))
        `when`(spotService.saveSpot(spot = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.parkCarOnSpot(parking = parking)).verifyComplete()

        verify(spotService, atLeastOnce()).saveSpot(spot = spotCaptor.capture())

        assertThat(
            spotCaptor.firstValue, allOf(
                instanceOf(Spot::class.java),
                hasProperty("id", equalTo(parking.spot.id)),
                hasProperty("sector", equalTo(parking.spot.sector)),
                hasProperty("latitude", equalTo(parking.spot.latitude)),
                hasProperty("longitude", equalTo(parking.spot.longitude)),
                hasProperty("occupied", equalTo(true))
            )
        )
    }

    @Test
    fun shouldAskSpotServiceToFindSpotByIdWhenSaveSpotAsOccupied() {
        val spot = buildSomeSpot()
        val parking = buildVacantAvailableParking()
        `when`(repository.save(parking = eq(parking))).thenReturn(Mono.just(parking))
        `when`(spotService.saveSpot(spot = any())).thenReturn(Mono.just(spot))
        `when`(spotService.findSpotBy(id = eq(spot.id!!))).thenReturn(Mono.empty())

        StepVerifier.create(service.parkCarOnSpot(parking = parking)).verifyComplete()

        verify(spotService, atLeastOnce()).findSpotBy(id = eq(spot.id))
    }

    @Test
    fun shouldNeverAskSectorServiceToCloseSectorOperationWhenOccupiedSpotBelongsToSectorThatHasNotReachedItsMaxCapacity() {
        val spot = buildSomeSpot()
        val parking = buildVacantAvailableParking()
        `when`(repository.save(parking = eq(parking))).thenReturn(Mono.just(parking))
        `when`(spotService.saveSpot(spot = any())).thenReturn(Mono.just(spot))
        `when`(spotService.findSpotBy(id = eq(spot.id!!))).thenReturn(Mono.just(spot))

        StepVerifier.create(service.parkCarOnSpot(parking = parking))
            .assertNext { parking -> parking }
            .verifyComplete()

        verify(sectorService, never()).closeSectorOperation(sector = any())
    }

    @Test
    fun shouldAskSectorServiceToCloseSectorOperationWhenOccupiedSpotBelongsToSectorThatHasReachedItsMaxCapacity() {
        val sector = buildSomeSector(maxCapacity = 2, spots = listOf(buildSomeSpot(), buildSomeSpot(), buildSomeSpot()))
        val spot = buildSomeSpot(sector = sector)
        val parking = buildVacantAvailableParking()
        `when`(repository.save(parking = eq(parking))).thenReturn(Mono.just(parking))
        `when`(spotService.saveSpot(spot = any())).thenReturn(Mono.just(spot))
        `when`(spotService.findSpotBy(id = eq(spot.id!!))).thenReturn(Mono.just(spot))
        `when`(sectorService.closeSectorOperation(sector = eq(sector))).thenReturn(Mono.empty())

        StepVerifier.create(service.parkCarOnSpot(parking = parking))
            .assertNext { parking -> parking }
            .verifyComplete()

        verify(sectorService, atLeastOnce()).closeSectorOperation(sector = eq(sector))
    }

    @Test
    fun shouldReturnSameParkingOnCloseSectorOperationWhenOccupiedSpotBelongsToSectorThatHasReachedItsMaxCapacity() {
        val sector = buildSomeSector(maxCapacity = 2, spots = listOf(buildSomeSpot(), buildSomeSpot(), buildSomeSpot()))
        val spot = buildSomeSpot(sector = sector)
        val parking = buildVacantAvailableParking()
        `when`(repository.save(parking = eq(parking))).thenReturn(Mono.just(parking))
        `when`(spotService.saveSpot(spot = any())).thenReturn(Mono.just(spot))
        `when`(spotService.findSpotBy(id = eq(spot.id!!))).thenReturn(Mono.just(spot))
        `when`(sectorService.closeSectorOperation(sector = eq(sector))).thenReturn(Mono.empty())

        StepVerifier.create(service.parkCarOnSpot(parking = parking))
            .assertNext { parking ->
                assertThat(parking,allOf(
                    instanceOf(Parking::class.java),
                    hasProperty("id", equalTo(parking.id)),
                    hasProperty("spot", equalTo(parking.spot)),
                    hasProperty("licensePlate", equalTo(parking.licensePlate)),
                    hasProperty("parkingTime", equalTo(parking.parkingTime)),
                    hasProperty("unparkingTime", allOf(notNullValue(), instanceOf<LocalDateTime>(LocalDateTime::class.java))),
                    hasProperty("priceRule", equalTo(parking.priceRule)),
                    hasProperty("status", equalTo(ParkingStatus.LEAVED))
                ))
            }
    }

    @Test
    fun shouldAskParkingRepositoryToSaveParkWhenUnparkingCarFromSpot() {
        val parking = buildSomeParking()
        `when`(repository.save(parking = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.unparkCarFromSpot(parking = parking)).verifyComplete()

        verify(repository, atLeastOnce()).save(parking = parkingCaptor.capture())

        assertThat(parkingCaptor.firstValue,allOf(
            instanceOf(Parking::class.java),
            hasProperty("id", equalTo(parking.id)),
            hasProperty("spot", equalTo(parking.spot)),
            hasProperty("licensePlate", equalTo(parking.licensePlate)),
            hasProperty("parkingTime", equalTo(parking.parkingTime)),
            hasProperty("unparkingTime", allOf(notNullValue(), instanceOf<LocalDateTime>(LocalDateTime::class.java))),
            hasProperty("priceRule", equalTo(parking.priceRule)),
            hasProperty("status", equalTo(ParkingStatus.LEAVED))
        ))
    }

    @Test
    fun shouldAskSpotServiceToSaveUpdatedParkWhenUnparkingCarFromSpot() {
        val parking = buildSomeParking()
        `when`(repository.save(parking = any())).thenReturn(Mono.just(parking))
        `when`(spotService.saveSpot(spot = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.unparkCarFromSpot(parking = parking)).verifyComplete()

        verify(spotService, atLeastOnce()).saveSpot(spot = spotCaptor.capture())

        assertThat(
            spotCaptor.firstValue, allOf(
                instanceOf(Spot::class.java),
                hasProperty("id", equalTo(parking.spot.id)),
                hasProperty("sector", equalTo(parking.spot.sector)),
                hasProperty("latitude", equalTo(parking.spot.latitude)),
                hasProperty("longitude", equalTo(parking.spot.longitude)),
                hasProperty("occupied", equalTo(false))
            )
        )
    }

    @Test
    fun shouldAskSpotServiceToFindSpotByIdWhenSaveSpotAsVacantAgain() {
        val spot = buildSomeSpot()
        val parking = buildSomeParking()
        `when`(repository.save(parking = any())).thenReturn(Mono.just(parking))
        `when`(spotService.saveSpot(spot = any())).thenReturn(Mono.just(spot))
        `when`(spotService.findSpotBy(id = eq(spot.id!!))).thenReturn(Mono.empty())

        StepVerifier.create(service.unparkCarFromSpot(parking = parking)).verifyComplete()

        verify(spotService, atLeastOnce()).findSpotBy(id = eq(spot.id))
    }

    @Test
    fun shouldNeverAskSectorServiceToReopenSectorOperationWhenVacantSpotBelongsToSectorThatHasReachedItsMaxCapacity() {
        val sector = buildSomeSector(maxCapacity = 2, spots = listOf(buildSomeSpot(), buildSomeSpot(), buildSomeSpot()))
        val spot = buildSomeSpot(sector = sector)
        val parking = buildVacantAvailableParking()
        `when`(repository.save(parking = any())).thenReturn(Mono.just(parking))
        `when`(spotService.saveSpot(spot = any())).thenReturn(Mono.just(spot))
        `when`(spotService.findSpotBy(id = eq(spot.id!!))).thenReturn(Mono.just(spot))
        `when`(sectorService.reopenSectorOperation(sector = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.unparkCarFromSpot(parking = parking))
            .assertNext { parking -> parking }
            .verifyComplete()

        verify(sectorService, never()).reopenSectorOperation(sector = any())
    }

    @Test
    fun shouldAskSectorServiceToReopenSectorOperationWhenVacantSpotBelongsToSectorThatHasNotReachedItsMaxCapacity() {
        val spot = buildSomeSpot()
        val parking = buildSomeParking()
        `when`(repository.save(parking = any())).thenReturn(Mono.just(parking))
        `when`(spotService.saveSpot(spot = any())).thenReturn(Mono.just(spot))
        `when`(spotService.findSpotBy(id = eq(spot.id!!))).thenReturn(Mono.just(spot))
        `when`(sectorService.reopenSectorOperation(sector = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.unparkCarFromSpot(parking = parking))
            .verifyComplete()

        verify(sectorService, atLeastOnce()).reopenSectorOperation(sector = sectorCaptor.capture())

        assertThat(sectorCaptor.firstValue,allOf(
            instanceOf(Sector::class.java),
            hasProperty("name", equalTo(parking.spot.sector!!.name)),
            hasProperty("basePrice", equalTo(parking.spot.sector.basePrice)),
            hasProperty("openHour", equalTo(parking.spot.sector.openHour)),
            hasProperty("closedHour", equalTo(parking.spot.sector.closedHour)),
            hasProperty("maxCapacity", equalTo(parking.spot.sector.maxCapacity)),
            hasProperty("durationLimitMinutes", equalTo(parking.spot.sector.durationLimitMinutes))
        ))
    }

    @Test
    fun shouldReturnSameParkingOnReopenSectorOperationWhenVacantSpotBelongsToSectorThatHasNotReachedItsMaxCapacity() {
        val spot = buildSomeSpot()
        val parking = buildSomeParking()
        `when`(repository.save(parking = any())).thenReturn(Mono.just(parking))
        `when`(spotService.saveSpot(spot = any())).thenReturn(Mono.just(spot))
        `when`(spotService.findSpotBy(id = eq(spot.id!!))).thenReturn(Mono.just(spot))
        `when`(sectorService.reopenSectorOperation(sector = any())).thenReturn(Mono.empty())

        StepVerifier.create(service.unparkCarFromSpot(parking = parking))
            .assertNext { parking ->
                assertThat(parking,allOf(
                    instanceOf(Parking::class.java),
                    hasProperty("id", equalTo(parking.id)),
                    hasProperty("spot", equalTo(parking.spot)),
                    hasProperty("licensePlate", equalTo(parking.licensePlate)),
                    hasProperty("parkingTime", equalTo(parking.parkingTime)),
                    hasProperty("unparkingTime", allOf(notNullValue(), instanceOf<LocalDateTime>(LocalDateTime::class.java))),
                    hasProperty("priceRule", equalTo(parking.priceRule)),
                    hasProperty("status", equalTo(ParkingStatus.LEAVED))
                ))
            }
    }

}