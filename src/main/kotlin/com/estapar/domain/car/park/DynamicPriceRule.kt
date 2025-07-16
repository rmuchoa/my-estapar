package com.estapar.domain.car.park

import java.math.BigDecimal

enum class DynamicPriceRule(val priceRate: Int, val priceRateDouble: Double) {

    TWENTY_FIVE_PERCENT_CAPACITY(priceRate = -10, priceRateDouble = -10.0),
    FIFTY_PERCENT_CAPACITY(priceRate = 0, priceRateDouble = 0.0),
    SEVENTY_FIVE_PERCENT_CAPACITY(priceRate = 10, priceRateDouble = 10.0),
    ONE_HUNDRED_PERCENT_CAPACITY(priceRate = 25, priceRateDouble = 25.0);

    companion object {
        fun of(priceLevelRate: Int): DynamicPriceRule =
            entries.find { type ->
                type.priceRate == priceLevelRate
            }?: ONE_HUNDRED_PERCENT_CAPACITY
    }

    fun getDecimalPriceRate(): BigDecimal =
        BigDecimal.valueOf(priceRateDouble)

    fun getPercentagePriceRate(): BigDecimal =
        getDecimalPriceRate().divide(BigDecimal.valueOf(100))

    fun applyPriceRatePercentageOn(price: BigDecimal): BigDecimal =
        price.minus(price.multiply(getPercentagePriceRate()))

}