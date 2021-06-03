package com.ycagri.buxassignment.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ycagri.buxassignment.R
import com.ycagri.buxassignment.db.ProductEntity
import com.ycagri.buxassignment.util.Resource
import com.ycagri.buxassignment.utils.CountingAppExecutorsRule
import com.ycagri.buxassignment.utils.DataBindingIdlingResourceRule
import com.ycagri.buxassignment.utils.TestUtil
import com.ycagri.buxassignment.utils.ViewModelUtil
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class ProductDetailFragmentTest {

    @Rule
    @JvmField
    val executorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val countingAppExecutors = CountingAppExecutorsRule()

    @Rule
    @JvmField
    val dataBindingIdlingResource = DataBindingIdlingResourceRule<ProductDetailFragment>()

    private val viewModel = Mockito.mock(ProductViewModel::class.java)

    private val product = MutableLiveData<Resource<ProductEntity>>()

    private val currentPrice = MutableLiveData<String>()

    private val closingPrice = MutableLiveData<String>()

    private val dayRangeLow = MutableLiveData<String>()

    private val dayRangeHigh = MutableLiveData<String>()

    private val yearRangeLow = MutableLiveData<String>()

    private val yearRangeHigh = MutableLiveData<String>()

    private val subscribed = MutableLiveData<Int>()

    @Before
    fun setup() {
        `when`(viewModel.product).thenReturn(product)
        `when`(viewModel.currentPrice).thenReturn(currentPrice)
        `when`(viewModel.closingPrice).thenReturn(closingPrice)
        `when`(viewModel.dayRangeLow).thenReturn(dayRangeLow)
        `when`(viewModel.dayRangeHigh).thenReturn(dayRangeHigh)
        `when`(viewModel.yearRangeLow).thenReturn(yearRangeLow)
        `when`(viewModel.yearRangeHigh).thenReturn(yearRangeHigh)
        `when`(viewModel.subscribed).thenReturn(subscribed)

        val scenario =
            launchFragmentInContainer(null, R.style.Theme_MaterialComponents_DayNight_DarkActionBar)
            {
                ProductDetailFragment().apply {
                    viewModelFactory = ViewModelUtil.createFor(viewModel)
                }
            }
        dataBindingIdlingResource.monitorFragment(scenario)
    }

    @Test
    fun testProductLoaded() {
        product.postValue(
            Resource.success(
                TestUtil.createProductEntity(
                    id = "id",
                    name = "Test Product",
                    symbol = "Test Symbol",
                    description = "Test Description"
                )
            )
        )

        onView(withId(R.id.tv_title)).check(ViewAssertions.matches(withText("Test Product")))
        onView(withId(R.id.tv_description)).check(ViewAssertions.matches(withText("Test Description")))
    }

    @Test
    fun testCurrentPriceLoaded() {
        currentPrice.postValue("Current Price")
        onView(withId(R.id.tv_current_price)).check(ViewAssertions.matches(withText("Current Price: Current Price")))
    }

    @Test
    fun testClosingPriceLoaded() {
        closingPrice.postValue("Closing Price")
        onView(withId(R.id.tv_closing_price)).check(ViewAssertions.matches(withText("Closing Price: Closing Price")))
    }

    @Test
    fun testDayRangeLowLoaded() {
        dayRangeLow.postValue("Day Range Low")
        onView(withId(R.id.tv_day_range_low)).check(ViewAssertions.matches(withText("Low: Day Range Low")))
    }

    @Test
    fun testDayRangeHighLoaded() {
        dayRangeHigh.postValue("Day Range High")
        onView(withId(R.id.tv_day_range_high)).check(ViewAssertions.matches(withText("High: Day Range High")))
    }

    @Test
    fun testYearRangeLowLoaded() {
        yearRangeLow.postValue("Year Range Low")
        onView(withId(R.id.tv_year_range_low)).check(ViewAssertions.matches(withText("Low: Year Range Low")))
    }

    @Test
    fun testYearRangeHighLoaded() {
        yearRangeHigh.postValue("Year Range High")
        onView(withId(R.id.tv_year_range_high)).check(ViewAssertions.matches(withText("High: Year Range High")))
    }

    @Test
    fun testSubscribe() {
        subscribed.postValue(1)
        onView(withId(R.id.btn_subscribe)).check(ViewAssertions.matches(withText(R.string.unsubscribe)))
    }

    @Test
    fun testUnsubscribe() {
        subscribed.postValue(0)
        onView(withId(R.id.btn_subscribe)).check(ViewAssertions.matches(withText(R.string.subscribe)))
    }
}