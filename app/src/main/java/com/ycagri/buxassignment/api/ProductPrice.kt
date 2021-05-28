package com.ycagri.buxassignment.api

import com.google.gson.annotations.SerializedName

data class ProductPrice (

    @SerializedName("currency")
    val currency: String,

    @SerializedName("decimals")
    val decimals: Int,

    @SerializedName("amount")
    val amount: Double
)