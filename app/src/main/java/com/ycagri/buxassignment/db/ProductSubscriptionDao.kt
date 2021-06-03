package com.ycagri.buxassignment.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
abstract class ProductSubscriptionDao {

    @Insert
    abstract fun insertSubscription(subscription: ProductSubscriptionEntity): Long

    @Update
    abstract fun updateSubscription(subscription: ProductSubscriptionEntity): Int

    @Query("Select * From tbl_product_subscriptions")
    abstract fun getSubscriptions(): LiveData<List<ProductSubscriptionEntity>>

    @Query("Select * From tbl_product_subscriptions Where product_id=:productId")
    abstract fun getProductSubscription(productId: String): LiveData<ProductSubscriptionEntity>
}