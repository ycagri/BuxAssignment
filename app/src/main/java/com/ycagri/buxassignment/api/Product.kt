package com.ycagri.buxassignment.api

import com.google.gson.annotations.SerializedName

data class Product(

    @SerializedName("symbol")
    val symbol: String,

    @SerializedName("displayName")
    val displayName: String,

    @SerializedName("securityId")
    val securityId: String,

    @SerializedName("quoteCurrency")
    val quoteCurrency: String?,

    @SerializedName("displayDecimals")
    val displayDecimals: Int?,

    @SerializedName("maxLeverage")
    val maxLeverage: Int?,

    @SerializedName("multiplier")
    val multiplier: Int?,

    @SerializedName("currentPrice")
    val currentPrice: ProductPrice,

    @SerializedName("closingPrice")
    val closingPrice: ProductPrice,

    @SerializedName("dayRange")
    val dayRange: ProductRange?,

    @SerializedName("yearRange")
    val yearRange: ProductRange?,

    @SerializedName("description")
    val description: String?
)