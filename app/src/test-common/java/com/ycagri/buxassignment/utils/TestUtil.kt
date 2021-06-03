package com.ycagri.buxassignment.utils

import com.ycagri.buxassignment.api.Product
import com.ycagri.buxassignment.api.ProductPrice
import com.ycagri.buxassignment.api.ProductRange
import com.ycagri.buxassignment.db.ProductEntity
import com.ycagri.buxassignment.db.ProductPriceEntity
import com.ycagri.buxassignment.db.ProductRangeEntity
import com.ycagri.buxassignment.db.ProductSubscriptionEntity

object TestUtil {

    fun createProductEntity(id: String, name: String, symbol: String, description: String) =
        ProductEntity(
            securityId = id,
            displayName = name,
            symbol = symbol,
            description = description,
            displayDecimals = 1,
            maxLeverage = 1,
            multiplier = 1,
            lastUpdateTime = 0,
            quoteCurrency = "currency"
        )

    fun createProductPriceEntity(productId: String, currency: String, type: Int) =
        ProductPriceEntity(
            id = 0,
            productId = productId,
            currency = currency,
            decimals = 1,
            amount = 10.0,
            type = type
        )

    fun createProductRangeEntity(productId: String, currency: String, type: Int) =
        ProductRangeEntity(
            id = 0,
            productId = productId,
            currency = currency,
            decimals = 1,
            high = 10.0,
            low = 1.0,
            type = type
        )

    fun createProductSubscriptionEntity(productId: String, subscribed: Int) =
        ProductSubscriptionEntity(
            id = 0,
            productId = productId,
            subscribed = subscribed
        )

    fun createProduct(
        id: String,
        symbol: String,
        name: String,
        currency: String,
        description: String
    ) = Product(
        symbol = symbol,
        displayName = name,
        securityId = id,
        quoteCurrency = currency,
        displayDecimals = null,
        maxLeverage = null,
        multiplier = null,
        description = description,
        currentPrice = ProductPrice(currency, 1, 10.0),
        closingPrice = ProductPrice(currency, 1, 10.0),
        dayRange = ProductRange(currency, 1, 0.0, 10.0),
        yearRange = ProductRange(currency, 1, 0.0, 10.0)
    )

    fun createProducts(
        count: Int,
        id: String,
        symbol: String,
        name: String,
        currency: String,
        description: String
    ) = (1..count).map {
        createProduct(
            id = "$id$it",
            symbol = "$symbol $it",
            name = "$name $it",
            currency = "$currency $it",
            description = "$description $it"
        )
    }
}