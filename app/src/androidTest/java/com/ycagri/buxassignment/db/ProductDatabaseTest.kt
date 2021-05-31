package com.ycagri.buxassignment.db

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ycagri.buxassignment.utils.TestUtil
import com.ycagri.buxassignment.utils.getOrAwaitValue
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class ProductDatabaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val countingTaskExecutorRule = CountingTaskExecutorRule()

    private lateinit var db: ProductDatabase

    @Before
    fun initDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ProductDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() {
        countingTaskExecutorRule.drainTasks(10, TimeUnit.SECONDS)
        db.close()
    }

    @Test
    fun testProductInsert() {
        assertEquals(1, insertProduct())
    }

    @Test
    fun testProductUpdate() {
        insertProduct()
        val product = TestUtil.createProductEntity(
            id = "id",
            name = "Test Product Update",
            symbol = "Test Symbol Update",
            description = "Test Description"
        )
        assertEquals(1, db.productDao().updateProduct(product))
    }

    @Test
    fun testProductGet() {
        insertProduct()
        val res = db.productDao().getProducts().getOrAwaitValue()
        assertEquals(1, res.size)
        assertEquals("id", res[0].securityId)
        assertEquals("Test Product", res[0].displayName)
        assertEquals("Test Symbol", res[0].symbol)
        assertEquals("Test Description", res[0].description)
    }

    @Test
    fun testProductGetById() {
        insertProduct()
        val res = db.productDao().getProductById("id").getOrAwaitValue()
        assertEquals("id", res.securityId)
        assertEquals("Test Product", res.displayName)
        assertEquals("Test Symbol", res.symbol)
        assertEquals("Test Description", res.description)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun testProductPriceInsertException() {
        insertProductPrice(ProductPriceEntity.CURRENT)
    }

    @Test
    fun testProductPriceInsert() {
        insertProduct()
        assertEquals(1, insertProductPrice(ProductPriceEntity.CURRENT))
    }

    @Test
    fun testProductPriceUpdate() {
        insertProduct()
        insertProductPrice(ProductPriceEntity.CURRENT)
        assertEquals(
            1, db.productPriceDao().updatePrice(
                currency = "Test Currency Update",
                decimals = 1,
                amount = 1.0,
                productId = "id",
                ProductPriceEntity.CURRENT
            )
        )
    }

    @Test
    fun testProductPriceCurrentGet() {
        insertProduct()
        assertEquals(1, insertProductPrice(ProductPriceEntity.CURRENT))
        val res = db.productPriceDao().getCurrentPrice("id").getOrAwaitValue()
        assertEquals("Test Currency", res.currency)
        assertEquals("id", res.productId)
        assertEquals(ProductPriceEntity.CURRENT, res.type)
    }

    @Test
    fun testProductPriceClosingGet() {
        insertProduct()
        assertEquals(1, insertProductPrice(ProductPriceEntity.CLOSING))
        val res = db.productPriceDao().getCurrentPrice("id").getOrAwaitValue()
        assertEquals("Test Currency", res.currency)
        assertEquals("id", res.productId)
        assertEquals(ProductPriceEntity.CLOSING, res.type)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun testProductRangeInsertException() {
        insertProductRange(ProductRangeEntity.DAY)
    }

    @Test
    fun testProductRangeInsert() {
        insertProduct()
        assertEquals(1, insertProductRange(ProductRangeEntity.DAY))
    }

    @Test
    fun testProductRangeUpdate() {
        insertProduct()
        insertProductRange(ProductRangeEntity.DAY)
        assertEquals(
            1,
            db.productRangeDao().updateRange(
                currency = "Test Currency Update",
                decimals = 1,
                high = 10.0,
                low = 1.0,
                productId = "id",
                type = ProductRangeEntity.DAY
            )
        )
    }

    @Test
    fun testProductRangeDayGet() {
        insertProduct()
        assertEquals(1, insertProductRange(ProductRangeEntity.DAY))
        val res = db.productRangeDao().getDayRange("id").getOrAwaitValue()
        assertEquals("Test Currency", res.currency)
        assertEquals("id", res.productId)
        assertEquals(ProductRangeEntity.DAY, res.type)
    }

    @Test
    fun testProductRangeYearGet() {
        insertProduct()
        assertEquals(1, insertProductRange(ProductRangeEntity.YEAR))
        val res = db.productRangeDao().getDayRange("id").getOrAwaitValue()
        assertEquals("Test Currency", res.currency)
        assertEquals("id", res.productId)
        assertEquals(ProductRangeEntity.YEAR, res.type)
    }

    @Test
    fun testInsertProducts() {
        val products = TestUtil.createProducts(
            count = 10,
            id = "test_id",
            symbol = "Test Symbol",
            name = "Test Product",
            currency = "Test Currency",
            description = "Test Description"
        )

        db.insertEntities(products)
        val res = db.productDao().getProducts().getOrAwaitValue()
        assertEquals(10, res.size)
        (0 until 10).forEach {
            assertEquals("test_id${it + 1}", res[it].securityId)
            assertEquals("Test Product ${it + 1}", res[it].displayName)
            assertEquals("Test Symbol ${it + 1}", res[it].symbol)
            assertEquals("Test Currency ${it + 1}", res[it].quoteCurrency)
            assertEquals("Test Description ${it + 1}", res[it].description)

            var price = db.productPriceDao().getClosingPrice("test_id${it + 1}").getOrAwaitValue()
            assertEquals("Test Currency ${it + 1}", price.currency)
            assertEquals(ProductPriceEntity.CLOSING, price.type)
            price = db.productPriceDao().getCurrentPrice("test_id${it + 1}").getOrAwaitValue()
            assertEquals("Test Currency ${it + 1}", price.currency)
            assertEquals(ProductPriceEntity.CURRENT, price.type)

            var range = db.productRangeDao().getDayRange("test_id${it + 1}").getOrAwaitValue()
            assertEquals("Test Currency ${it + 1}", range.currency)
            assertEquals(ProductRangeEntity.DAY, range.type)
            range = db.productRangeDao().getYearRange("test_id${it + 1}").getOrAwaitValue()
            assertEquals("Test Currency ${it + 1}", range.currency)
            assertEquals(ProductRangeEntity.YEAR, range.type)
        }
    }

    @Test
    fun testInsertOrUpdateProducts() {
        db.insertEntities(TestUtil.createProducts(
            count = 10,
            id = "test_id",
            symbol = "Test Symbol",
            name = "Test Product",
            currency = "Test Currency",
            description = "Test Description"
        ))

        val products =  TestUtil.createProducts(
            count = 10,
            id = "test_id",
            symbol = "Test Symbol Update",
            name = "Test Product Update",
            currency = "Test Currency Update",
            description = "Test Description Update"
        )
        db.insertEntities(products)

        val res = db.productDao().getProducts().getOrAwaitValue()
        assertEquals(10, res.size)
        (0 until 10).forEach {
            assertEquals("test_id${it + 1}", res[it].securityId)
            assertEquals("Test Product Update ${it + 1}", res[it].displayName)
            assertEquals("Test Symbol Update ${it + 1}", res[it].symbol)
            assertEquals("Test Currency Update ${it + 1}", res[it].quoteCurrency)
            assertEquals("Test Description Update ${it + 1}", res[it].description)

            var price = db.productPriceDao().getClosingPrice("test_id${it + 1}").getOrAwaitValue()
            assertEquals("Test Currency Update ${it + 1}", price.currency)
            assertEquals(ProductPriceEntity.CLOSING, price.type)
            price = db.productPriceDao().getCurrentPrice("test_id${it + 1}").getOrAwaitValue()
            assertEquals("Test Currency Update ${it + 1}", price.currency)
            assertEquals(ProductPriceEntity.CURRENT, price.type)

            var range = db.productRangeDao().getDayRange("test_id${it + 1}").getOrAwaitValue()
            assertEquals("Test Currency Update ${it + 1}", range.currency)
            assertEquals(ProductRangeEntity.DAY, range.type)
            range = db.productRangeDao().getYearRange("test_id${it + 1}").getOrAwaitValue()
            assertEquals("Test Currency Update ${it + 1}", range.currency)
            assertEquals(ProductRangeEntity.YEAR, range.type)
        }
    }

    private fun insertProduct(): Long {
        return db.productDao().insertProduct(
            TestUtil.createProductEntity(
                id = "id",
                name = "Test Product",
                symbol = "Test Symbol",
                description = "Test Description"
            )
        )
    }

    private fun insertProductPrice(type: Int): Long {
        return db.productPriceDao().insertPrice(
            TestUtil.createProductPriceEntity(
                productId = "id",
                currency = "Test Currency",
                type = type
            )
        )
    }

    private fun insertProductRange(type: Int): Long {
        return db.productRangeDao().insertRange(
            TestUtil.createProductRangeEntity(
                productId = "id",
                currency = "Test Currency",
                type = type
            )
        )
    }

}