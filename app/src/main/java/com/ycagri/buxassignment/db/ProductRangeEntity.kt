package com.ycagri.buxassignment.db

import androidx.room.*

@Entity(
    tableName = "tbl_product_ranges", foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["security_id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index(value = ["product_id"])]
)
data class ProductRangeEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "product_id")
    val productId: String,

    val currency: String,

    val decimals: Int,

    val high: Double,

    val low: Double,

    val type: Int
) {
    companion object {
        const val DAY = 0

        const val YEAR = 1
    }
}