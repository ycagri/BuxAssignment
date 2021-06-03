package com.ycagri.buxassignment.db

import androidx.room.*

@Entity(
    tableName = "tbl_product_subscriptions", foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["security_id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index(value = ["product_id"])]
)
class ProductSubscriptionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "product_id")
    val productId: String,

    val subscribed: Int
)