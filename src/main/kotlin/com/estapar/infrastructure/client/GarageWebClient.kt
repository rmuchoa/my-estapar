package com.estapar.infrastructure.client

import com.estapar.infrastructure.client.dto.LoadGarageResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Component
class GarageWebClient(
    builder: WebClient.Builder,
    @Value("\${client.garage.base-url}")
    val baseUrl: String
) {

    private val webClient =
        builder.baseUrl(baseUrl).build()

    fun getFirstGarageLoad(): Mono<LoadGarageResponse> =
        treatWebClientResponse(
            response = webClient.get()
                .uri("/garage")
                .retrieve(),
            type = LoadGarageResponse::class.java)

    private fun <T> treatWebClientResponse(response: WebClient.ResponseSpec, type: Class<T>) =
        response
            .onStatus(HttpStatusCode::is4xxClientError) { response ->
                response.bodyToMono<String>().flatMap {
                    Mono.error(RuntimeException("Erro 4xx: ${response.statusCode()} - $it"))
                }
            }
            .onStatus(HttpStatusCode::is5xxServerError) { response ->
                response.bodyToMono<String>().flatMap {
                    Mono.error(RuntimeException("Erro 5xx: ${response.statusCode()} - $it"))
                }
            }
            .bodyToMono(type)
            .onErrorResume { ex ->
                Mono.error(RuntimeException("Erro na chamada ao serviço de garagem: ${ex.message}", ex))
            }

}