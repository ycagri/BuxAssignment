package com.ycagri.buxassignment.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_products")
data class ProductEntity(

    val symbol: String,

    @ColumnInfo(name = "display_name")
    val displayName: String,

    @PrimaryKey
    @ColumnInfo(name = "security_id")
    val securityId: String,

    @ColumnInfo(name = "quote_currency")
    val quoteCurrency: String?,

    @ColumnInfo(name = "display_decimals")
    val displayDecimals: Int?,

    @ColumnInfo(name = "max_leverage")
    val maxLeverage: Int?,

    val multiplier: Int?,

    val description: String?,

    @ColumnInfo(name = "last_update")
    val lastUpdateTime: Long?
)