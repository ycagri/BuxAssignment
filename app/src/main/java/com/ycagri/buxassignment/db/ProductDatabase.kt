package com.ycagri.buxassignment.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ycagri.buxassignment.api.Product
import com.ycagri.buxassignment.api.ProductPrice
import com.ycagri.buxassignment.api.ProductRange

@Database(
    entities = [
        ProductEntity::class,
        ProductPriceEntity::class,
        ProductRangeEntity::class
    ],
    version = 1, exportSchema = false
)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    abstract fun productPriceDao(): ProductPriceDao

    abstract fun productRangeDao(): ProductRangeDao

    fun insertEntities(products: List<Product>) {
        runInTransaction {
            val updateTime = System.currentTimeMillis()
            products.forEach {
                insertOrUpdateProduct(it, updateTime)
                insertPrice(it.securityId, it.closingPrice, ProductPriceEntity.CLOSING)
                insertPrice(it.securityId, it.currentPrice, ProductPriceEntity.CURRENT)
                insertRange(it.securityId, it.dayRange, ProductRangeEntity.DAY)
                insertRange(it.securityId, it.dayRange, ProductRangeEntity.YEAR)
            }
        }
    }

    private fun insertOrUpdateProduct(product: Product, updateTime: Long) {
        val p = ProductEntity(
            securityId = product.securityId,
            displayName = product.displayName,
            symbol = product.symbol,
            quoteCurrency = product.quoteCurrency,
            multiplier = product.multiplier,
            maxLeverage = product.maxLeverage,
            displayDecimals = product.displayDecimals,
            description = product.description,
            lastUpdateTime = updateTime
        )

        if (productDao().updateProduct(p) == 0)
            productDao().insertProduct(p)
    }

    private fun insertPrice(productId: String, price: ProductPrice, type: Int) {
        if (productPriceDao().updatePrice(
                price.currency,
                price.decimals,
                price.amount,
                productId,
                type
            ) == 0
        ) {
            productPriceDao().insertPrice(
                ProductPriceEntity(
                    id = 0,
                    productId = productId,
                    currency = price.currency,
                    decimals = price.decimals,
                    amount = price.amount,
                    type = type
                )
            )
        }
    }

    private fun insertRange(productId: String, range: ProductRange?, type: Int) {
        range?.let {
            if (productRangeDao().updateRange(
                    it.currency,
                    it.decimals,
                    it.high,
                    it.low,
                    productId,
                    type
                ) == 0
            ) {
                productRangeDao().insertRange(
                    ProductRangeEntity(
                        id = 0,
                        productId = productId,
                        currency = it.currency,
                        decimals = it.decimals,
                        high = it.high,
                        low = it.low,
                        type = type
                    )
                )
            }
        }
    }
}