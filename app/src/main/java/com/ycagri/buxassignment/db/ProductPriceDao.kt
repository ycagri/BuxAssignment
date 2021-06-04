package com.ycagri.buxassignment.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class ProductPriceDao {

    @Insert
    abstract fun insertPrice(price: ProductPriceEntity): Long

    @Query("Update tbl_product_prices SET currency=:currency, decimals=:decimals, amount=:amount Where product_id=:productId And type=:type")
    abstract fun updatePrice(
        currency: String,
        decimals: Int,
        amount: Double,
        productId: String,
        type: Int
    ): Int

    @Query("Update tbl_product_prices SET amount=:amount Where product_id=:productId And type=0")
    abstract fun updateCurrentPrice(amount: Double, productId: String): Int

    @Query("Select * From tbl_product_prices Where product_id=:id And type=${ProductPriceEntity.CLOSING}")
    abstract fun getClosingPrice(id: String): LiveData<ProductPriceEntity>

    @Query("Select * From tbl_product_prices Where product_id=:id And type=${ProductPriceEntity.CURRENT}")
    abstract fun getCurrentPrice(id: String): LiveData<ProductPriceEntity>
}