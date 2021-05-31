package com.ycagri.buxassignment.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class ProductRangeDao {

    @Insert
    abstract fun insertRange(range: ProductRangeEntity): Long

    @Query("Update tbl_product_ranges Set currency=:currency, decimals=:decimals, high=:high, low=:low Where product_id=:productId And type=:type")
    abstract fun updateRange(
        currency: String,
        decimals: Int,
        high: Double,
        low: Double,
        productId: String,
        type: Int
    ): Int

    @Query("Select * From tbl_product_ranges Where product_id=:id And type=${ProductRangeEntity.DAY}")
    abstract fun getDayRange(id: String): LiveData<ProductRangeEntity>

    @Query("Select * From tbl_product_ranges Where product_id=:id And type=${ProductRangeEntity.YEAR}")
    abstract fun getYearRange(id: String): LiveData<ProductRangeEntity>
}