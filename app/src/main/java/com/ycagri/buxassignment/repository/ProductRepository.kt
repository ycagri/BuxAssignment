package com.ycagri.buxassignment.repository

import androidx.lifecycle.LiveData
import com.ycagri.buxassignment.api.ApiResponse
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

    fun getProduct(id: String) =
        object : NetworkBoundResource<ProductEntity, Product>(appExecutors) {

            override fun saveCallResult(item: Product) {
                db.updateProduct(item)
            }

            override fun shouldFetch(data: ProductEntity?) = true

            override fun loadFromDb() = db.productDao().getProductById(id)

            override fun createCall(): LiveData<ApiResponse<Product>> {
                return service.getProductById(id)
            }
        }.asLiveData()

    fun getClosingPrice(id: String) = db.productPriceDao().getClosingPrice(id)

    fun getCurrentPrice(id: String) = db.productPriceDao().getCurrentPrice(id)

    fun getDayRange(id: String) = db.productRangeDao().getDayRange(id)

    fun getYearRange(id: String) = db.productRangeDao().getYearRange(id)
}