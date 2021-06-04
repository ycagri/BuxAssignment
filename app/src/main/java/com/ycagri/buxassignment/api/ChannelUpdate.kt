package com.ycagri.buxassignment.api

import com.google.gson.annotations.SerializedName

data class ChannelUpdate(

    @SerializedName("t")
    val trading: String,

    @SerializedName("id")
    val id: String,

    @SerializedName("v")
    val version: Int,

    @SerializedName("body")
    val body: ChannelUpdateBody
)

data class ChannelUpdateBody(

    @SerializedName("securityId")
    val securityId: String?,

    @SerializedName("currentPrice")
    val currentPrice: String?
)