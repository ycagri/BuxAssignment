package com.ycagri.buxassignment.api

import androidx.lifecycle.LiveData
import retrofit2.http.GET
import retrofit2.http.Path

interface BuxRetrofitApi {

    @GET("core/21/products/")
    fun getProducts(): LiveData<ApiResponse<List<Product>>>

    @GET("core/21/products/{id}")
    fun getProductById(@Path("id") id: String): LiveData<ApiResponse<Product>>
}