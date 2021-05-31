package com.ycagri.buxassignment.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ycagri.buxassignment.db.ProductEntity
import com.ycagri.buxassignment.repository.ProductRepository
import com.ycagri.buxassignment.util.Resource
import com.ycagri.buxassignment.utils.TestUtil
import com.ycagri.buxassignment.utils.mock
import org.junit.Assert.assertNotNull
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
}