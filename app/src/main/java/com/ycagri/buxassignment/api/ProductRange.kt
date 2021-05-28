package com.ycagri.buxassignment.api

import com.google.gson.annotations.SerializedName

data class ProductRange(
    @SerializedName("currency")
    val currency: String,

    @SerializedName("decimals")
    val decimals: Int,

    @SerializedName("high")
    val high: Double,

    @SerializedName("low")
    val low: Double
)