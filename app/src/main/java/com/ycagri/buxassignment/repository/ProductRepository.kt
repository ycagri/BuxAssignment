package com.ycagri.buxassignment.repository

import androidx.lifecycle.LiveData
import com.ycagri.buxassignment.api.ApiResponse
import com.ycagri.buxassignment.api.BuxRetrofitApi
import com.ycagri.buxassignment.api.Product
import com.ycagri.buxassignment.api.SubscriptionListener
import com.ycagri.buxassignment.db.ProductDatabase
import com.ycagri.buxassignment.db.ProductEntity
import com.ycagri.buxassignment.db.ProductSubscriptionEntity
import com.ycagri.buxassignment.testing.OpenForTesting
import com.ycagri.buxassignment.util.AppExecutors
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject

@OpenForTesting
class ProductRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val service: BuxRetrofitApi,
    private val db: ProductDatabase,
    private val client: OkHttpClient
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

    fun getProductsFromDatabase() = db.productDao().getProducts()

    fun getClosingPrice(id: String) = db.productPriceDao().getClosingPrice(id)

    fun getCurrentPrice(id: String) = db.productPriceDao().getCurrentPrice(id)

    fun getDayRange(id: String) = db.productRangeDao().getDayRange(id)

    fun getYearRange(id: String) = db.productRangeDao().getYearRange(id)

    fun insertSubscription(subscription: ProductSubscriptionEntity) =
        db.productSubscriptionDao().insertSubscription(subscription)

    fun updateSubscription(subscription: ProductSubscriptionEntity) =
        db.productSubscriptionDao().updateSubscription(subscription)

    fun getSubscription(id: String) = db.productSubscriptionDao().getProductSubscription(id)

    fun getSubscriptionConnection(): WebSocket {
        val request = Request.Builder()
            .url("https://rtf.beta.getbux.com/subscriptions/me")
            .addHeader(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyZWZyZXNoYWJsZSI6ZmFsc2UsInN1YiI6ImJiMGNkYTJiLWExMGUtNGVkMy1hZDVhLTBmODJiNGMxNTJjNCIsImF1ZCI6ImJldGEuZ2V0YnV4LmNvbSIsInNjcCI6WyJhcHA6bG9naW4iLCJydGY6bG9naW4iXSwiZXhwIjoxODIwODQ5Mjc5LCJpYXQiOjE1MDU0ODkyNzksImp0aSI6ImI3MzlmYjgwLTM1NzUtNGIwMS04NzUxLTMzZDFhNGRjOGY5MiIsImNpZCI6Ijg0NzM2MjI5MzkifQ.M5oANIi2nBtSfIfhyUMqJnex-JYg6Sm92KPYaUL9GKg"
            )
            .addHeader("Accept-Language", "nl-NL,en;q=0.8")
            .build();
        return client.newWebSocket(request, SubscriptionListener())
    }
}