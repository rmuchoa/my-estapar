package com.estapar.domain.park

enum class DynamicPriceRule(val priceRate: Int) {

    TWENTY_FIVE_PERCENT_CAPACITY(priceRate = -10),
    FIFTY_PERCENT_CAPACITY(priceRate = 0),
    SEVENTY_FIVE_PERCENT_CAPACITY(priceRate = 10),
    ONE_HUNDRED_PERCENT_CAPACITY(priceRate = 25)

}