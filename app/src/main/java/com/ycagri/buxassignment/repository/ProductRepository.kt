package com.ycagri.buxassignment.repository

import com.ycagri.buxassignment.api.BuxRetrofitApi
import com.ycagri.buxassignment.testing.OpenForTesting
import javax.inject.Inject

@OpenForTesting
class ProductRepository @Inject constructor(private val service: BuxRetrofitApi) {


}