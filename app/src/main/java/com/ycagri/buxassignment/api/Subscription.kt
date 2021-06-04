package com.ycagri.buxassignment.api

import com.google.gson.annotations.SerializedName

class Subscription(

    @SerializedName("subscribeTo")
    val subscribeList: MutableList<String>,

    @SerializedName("unsubscribeFrom")
    val unsubscribeList: MutableList<String>
)