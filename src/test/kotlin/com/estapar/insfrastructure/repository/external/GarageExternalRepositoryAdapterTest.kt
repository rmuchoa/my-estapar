package com.estapar.insfrastructure.repository.external

import com.estapar.domain.garage.Garage
import com.estapar.domain.garage.sector.Sector
import com.estapar.domain.garage.sector.SectorStatus
import com.estapar.domain.garage.spot.Spot
import com.estapar.infrastructure.client.dto.GarageSectorDTO
import com.estapar.infrastructure.client.GarageWebClient
import com.estapar.infrastructure.client.dto.LoadGarageResponse
import com.estapar.infrastructure.client.dto.SpotDTO
import com.estapar.infrastructure.repository.external.GarageExternalRepositoryAdapter
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.time.LocalTime

@ExtendWith(MockitoExtension::class)
class GarageExternalRepositoryAdapterTest(
    @Mock private val webClient: GarageWebClient
) {

    private lateinit var repositoryAdapter: GarageExternalRepositoryAdapter

    @BeforeEach
    fun setUp() {
        repositoryAdapter = GarageExternalRepositoryAdapter(webClient)
    }

    @Test
    fun shouldAskWebClientToGetFirstGarageLoadWhenLoadingGarageFirstTime() {
        `when`(webClient.getFirstGarageLoad()).thenReturn(Mono.empty())

        StepVerifier.create(repositoryAdapter.loadGarageFirstTime())
            .verifyComplete()

        verify(webClient, atLeastOnce()).getFirstGarageLoad()
    }

    @Test
    fun shouldConvertReceivedGarageWithSectorsAndSpotsWhenLoadingGarageFirstTime() {
        val openHour = LocalTime.of(9, 10)
        val closeHour = LocalTime.of(15, 45)
        val sector = GarageSectorDTO(sector = "A", basePrice = BigDecimal.valueOf(30.24), maxCapacity = 100, openHour, closeHour, durationLimitMinutes = 20)
        val spot = SpotDTO(id = 1, sector = "A", latitude = 20.34, longitude = 45.29, occupied = true)
        val garage = LoadGarageResponse(garage = listOf(sector), spots = listOf(spot))
        `when`(webClient.getFirstGarageLoad()).thenReturn(Mono.just(garage))

        StepVerifier.create(repositoryAdapter.loadGarageFirstTime())
            .assertNext { garage ->
                assertThat(garage,allOf(
                    instanceOf(Garage::class.java),
                    hasProperty("sectors",allOf(
                        instanceOf<List<Sector>>(List::class.java),
                        hasSize(1),
                        contains(allOf(
                            instanceOf(Sector::class.java),
                            hasProperty("id", nullValue()),
                            hasProperty("name", equalTo(sector.sector)),
                            hasProperty("basePrice", equalTo(sector.basePrice)),
                            hasProperty("openHour", equalTo(sector.openHour)),
                            hasProperty("closeHour", equalTo(sector.closeHour)),
                            hasProperty("maxCapacity", equalTo(sector.maxCapacity)),
                            hasProperty("durationLimitMinutes", equalTo(sector.durationLimitMinutes)),
                            hasProperty("status", equalTo(SectorStatus.OPENED)),
                        ))
                    )),
                    hasProperty("spots", allOf(
                        instanceOf<List<Spot>>(List::class.java),
                        hasSize(1),
                        contains(allOf(
                            instanceOf(Spot::class.java),
                            hasProperty("id", nullValue()),
                            hasProperty("sector", instanceOf<Sector>(Sector::class.java)),
                            hasProperty("latitude", equalTo(spot.latitude)),
                            hasProperty("longitude", equalTo(spot.longitude)),
                            hasProperty("occupied", equalTo(spot.occupied))
                        ))
                    ))
                ))
            }
            .verifyComplete()
    }

}