package com.estapar.infrastructure.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
class TransactionManagerConfig(
    private val connectionFactory: ConnectionFactory
) {

    @Bean
    fun transactionManager(): R2dbcTransactionManager =
        R2dbcTransactionManager(connectionFactory)

}