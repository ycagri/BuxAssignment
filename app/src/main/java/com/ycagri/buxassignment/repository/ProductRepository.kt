package com.ycagri.buxassignment.repository

import com.ycagri.buxassignment.api.BuxRetrofitApi
import com.ycagri.buxassignment.api.Product
import com.ycagri.buxassignment.db.ProductDatabase
import com.ycagri.buxassignment.db.ProductEntity
import com.ycagri.buxassignment.testing.OpenForTesting
import com.ycagri.buxassignment.util.AppExecutors
import javax.inject.Inject

@OpenForTesting
class ProductRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val service: BuxRetrofitApi,
    private val db: ProductDatabase
) {

    fun getProducts() =
        object : NetworkBoundResource<List<ProductEntity>, List<Product>>(appExecutors) {
            override fun saveCallResult(item: List<Product>) {
                db.insertEntities(item)
            }

            override fun shouldFetch(data: List<ProductEntity>?) = true

            override fun loadFromDb() = db.productDao().getProducts()

            override fun createCall() = service.getProducts()
        }.asLiveData()
}