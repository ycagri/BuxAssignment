package com.ycagri.buxassignment.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ycagri.buxassignment.db.ProductEntity
import com.ycagri.buxassignment.db.ProductPriceEntity
import com.ycagri.buxassignment.db.ProductRangeEntity
import com.ycagri.buxassignment.repository.ProductRepository
import com.ycagri.buxassignment.util.Resource
import com.ycagri.buxassignment.utils.TestUtil
import com.ycagri.buxassignment.utils.mock
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class ProductViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val productRepository = mock(ProductRepository::class.java)

    private val productViewModel = ProductViewModel(productRepository)


    @Before
    fun setup(){
        `when`(productRepository.getProduct("id")).thenReturn(
            MutableLiveData(
                Resource.success(
                    TestUtil.createProductEntity(
                        id = "id",
                        name = "Test Product",
                        symbol = "Test Symbol",
                        description = "Test Description"
                    )
                )
            )
        )

        `when`(productRepository.getCurrentPrice("id")).thenReturn(
            MutableLiveData(
                TestUtil.createProductPriceEntity(
                    productId = "id",
                    currency = "Test Currency",
                    type = ProductPriceEntity.CURRENT
                )
            )
        )

        `when`(productRepository.getClosingPrice("id")).thenReturn(
            MutableLiveData(
                TestUtil.createProductPriceEntity(
                    productId = "id",
                    currency = "Test Currency",
                    type = ProductPriceEntity.CURRENT
                )
            )
        )

        `when`(productRepository.getDayRange("id")).thenReturn(
            MutableLiveData(
                TestUtil.createProductRangeEntity(
                    productId = "id",
                    currency = "Test Currency",
                    type = ProductRangeEntity.DAY
                )
            )
        )

        `when`(productRepository.getYearRange("id")).thenReturn(
            MutableLiveData(
                TestUtil.createProductRangeEntity(
                    productId = "id",
                    currency = "Test Currency",
                    type = ProductRangeEntity.YEAR
                )
            )
        )
    }

    @Test
    fun testNull() {
        `when`(productRepository.getProducts()).thenReturn(mock())
        assertNotNull(productViewModel.products)
    }

    @Test
    fun testCallRepo() {
        `when`(productRepository.getProducts()).thenReturn(mock())
        productViewModel.products.observeForever(mock())
        verify(productRepository).getProducts()
    }

    @Test
    fun sendProductsToUI() {
        val products = MutableLiveData<Resource<List<ProductEntity>>>()
        `when`(productRepository.getProducts()).thenReturn(products)
        val observer = mock<Observer<Resource<List<ProductEntity>>>>()
        productViewModel.products.observeForever(observer)
        verify(observer, never()).onChanged(any())
        val result = TestUtil.createProductEntity(
            id = "id",
            name = "Test Product",
            symbol = "Test Symbol",
            description = "Test Description"
        )
        val value = Resource.success(listOf(result))
        products.value = value
        verify(observer).onChanged(value)
        reset(observer)
    }

    @Test
    fun testCallProduct() {
        productViewModel.product.observeForever(mock())
        productViewModel.selectProduct("id")
        verify(productRepository).getProduct("id")
    }

    @Test
    fun testCallCurrentPrice() {
        productViewModel.currentPrice.observeForever(mock())
        productViewModel.selectProduct("id")
        verify(productRepository).getCurrentPrice("id")
    }

    @Test
    fun testSendCurrentPriceToUI() {
        val observer = mock<Observer<String>>()
        productViewModel.currentPrice.observeForever(observer)
        productViewModel.selectProduct("id")
        verify(productRepository).getCurrentPrice("id")
        verify(observer).onChanged("10.0 (Test Currency)")
    }

    @Test
    fun testCallClosingPrice() {
        productViewModel.closingPrice.observeForever(mock())
        productViewModel.selectProduct("id")
        verify(productRepository).getClosingPrice("id")
    }

    @Test
    fun testSendClosingPriceToUI() {
        val observer = mock<Observer<String>>()
        productViewModel.closingPrice.observeForever(observer)
        productViewModel.selectProduct("id")
        verify(productRepository).getClosingPrice("id")
        verify(observer).onChanged("10.0 (Test Currency)")
    }

    @Test
    fun testCallDayRangeLowPrice() {
        productViewModel.dayRangeLow.observeForever(mock())
        productViewModel.selectProduct("id")
        verify(productRepository).getDayRange("id")
    }

    @Test
    fun testSendDayRangeLowToUI() {
        val observer = mock<Observer<String>>()
        productViewModel.dayRangeLow.observeForever(observer)
        productViewModel.selectProduct("id")
        verify(productRepository).getDayRange("id")
        verify(observer).onChanged("1.0 (Test Currency)")
    }

    @Test
    fun testCallDayRangeHighPrice() {
        productViewModel.dayRangeHigh.observeForever(mock())
        productViewModel.selectProduct("id")
        verify(productRepository).getDayRange("id")
    }

    @Test
    fun testSendDayRangeHighToUI() {
        val observer = mock<Observer<String>>()
        productViewModel.dayRangeHigh.observeForever(observer)
        productViewModel.selectProduct("id")
        verify(productRepository).getDayRange("id")
        verify(observer).onChanged("10.0 (Test Currency)")
    }

    @Test
    fun testCallYearRangeLowPrice() {
        productViewModel.yearRangeLow.observeForever(mock())
        productViewModel.selectProduct("id")
        verify(productRepository).getYearRange("id")
    }

    @Test
    fun testSendYearRangeLowToUI() {
        val observer = mock<Observer<String>>()
        productViewModel.yearRangeLow.observeForever(observer)
        productViewModel.selectProduct("id")
        verify(productRepository).getYearRange("id")
        verify(observer).onChanged("1.0 (Test Currency)")
    }

    @Test
    fun testCallYearRangeHighPrice() {
        productViewModel.yearRangeHigh.observeForever(mock())
        productViewModel.selectProduct("id")
        verify(productRepository).getYearRange("id")
    }

    @Test
    fun testSendYearRangeHighToUI() {
        val observer = mock<Observer<String>>()
        productViewModel.yearRangeHigh.observeForever(observer)
        productViewModel.selectProduct("id")
        verify(productRepository).getYearRange("id")
        verify(observer).onChanged("10.0 (Test Currency)")
    }
}