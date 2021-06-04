package com.ycagri.buxassignment.db

import androidx.room.*

@Entity(tableName = "tbl_product_prices",foreignKeys = [
    ForeignKey(entity = ProductEntity::class,
        parentColumns = ["security_id"],
        childColumns = ["product_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["product_id"])])
data class ProductPriceEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "product_id")
    val productId: String,

    val currency: String,

    val decimals: Int,

    val amount: Double,

    val type: Int
){
    companion object{
        const val CURRENT = 0
        const val CLOSING = 1
    }
}