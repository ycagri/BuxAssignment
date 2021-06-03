package com.ycagri.buxassignment.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ycagri.buxassignment.util.LiveDataCallAdapterFactory
import com.ycagri.buxassignment.utils.getOrAwaitValue
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class BuxRetrofitApiTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: BuxRetrofitApi

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(BuxRetrofitApi::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getProduct() {
        enqueueResponse("product.json")
        val product = (service.getProductById("id").getOrAwaitValue() as ApiSuccessResponse).body

        val request = mockWebServer.takeRequest()
        assertEquals("/core/21/products/id", request.path)

        assertNotNull(product)
        assertEquals("China A50", product.displayName)
        assertEquals("China A50", product.symbol)
        assertEquals("sb42723", product.securityId)
        assertEquals(18255.0, product.currentPrice.amount)
        assertEquals(18173.0, product.closingPrice.amount)
    }

    @Test
    fun getProducts() {
        enqueueResponse("products.json")
        val products = (service.getProducts().getOrAwaitValue() as ApiSuccessResponse).body

        val request = mockWebServer.takeRequest()
        assertEquals("/core/21/products/", request.path)

        assertNotNull(products)
        assertEquals(43, products.size)
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream("api-response/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }
}