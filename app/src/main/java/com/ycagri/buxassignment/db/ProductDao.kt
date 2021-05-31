package com.ycagri.buxassignment.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
abstract class ProductDao {

    @Insert
    abstract fun insertProduct(product: ProductEntity): Long

    @Update
    abstract fun updateProduct(product: ProductEntity): Int

    @Query("Select * From tbl_products")
    abstract fun getProducts(): LiveData<List<ProductEntity>>

    @Query("Select * From tbl_products Where security_id=:id")
    abstract fun getProductById(id: String): LiveData<ProductEntity>
}