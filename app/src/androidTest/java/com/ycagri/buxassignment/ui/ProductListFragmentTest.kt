package com.ycagri.buxassignment.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ycagri.buxassignment.R
import com.ycagri.buxassignment.db.ProductEntity
import com.ycagri.buxassignment.util.Resource
import com.ycagri.buxassignment.utils.CountingAppExecutorsRule
import com.ycagri.buxassignment.utils.RecyclerViewMatcher
import com.ycagri.buxassignment.utils.SwipeRefreshLayoutMatchers.isRefreshing
import com.ycagri.buxassignment.utils.TestUtil
import com.ycagri.buxassignment.utils.ViewModelUtil
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class ProductListFragmentTest {

    @Rule
    @JvmField
    val executorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val countingAppExecutors = CountingAppExecutorsRule()

    private val viewModel = mock(ProductViewModel::class.java)

    private val products = MutableLiveData<Resource<List<ProductEntity>>>()

    @Before
    fun setup() {
        `when`(viewModel.products).thenReturn(products)
        launchFragmentInContainer(null, R.style.Theme_MaterialComponents_DayNight_DarkActionBar) {
            ProductListFragment().apply {
                viewModelFactory = ViewModelUtil.createFor(viewModel)
                appExecutors = countingAppExecutors.appExecutors
            }
        }
    }

    @Test
    fun testLoading() {
        products.postValue(Resource.loading(null))
        onView(withId(R.id.srl_products)).check(ViewAssertions.matches(isRefreshing()))
    }

    @Test
    fun testLoaded() {
        products.postValue(
            Resource.success(
                listOf(
                    TestUtil.createProductEntity(
                        id = "id",
                        name = "Test Product",
                        symbol = "Test Symbol",
                        description = "Test Description"
                    )
                )
            )
        )

        onView(withId(R.id.srl_products)).check(ViewAssertions.matches(not(isRefreshing())))
        listMatcher().atPosition(0, R.id.item_detail)
            .matches(ViewAssertions.matches(withText("Test Product")))
    }

    @Test
    fun testError() {
        products.postValue(Resource.error("Test Error!", null))
        onView(withId(R.id.srl_products)).check(ViewAssertions.matches(not(isRefreshing())))
        onView(withText("Test Error!")).check(
            ViewAssertions.matches(
                ViewMatchers.withEffectiveVisibility(
                    ViewMatchers.Visibility.VISIBLE
                )
            )
        )
    }

    private fun listMatcher() = RecyclerViewMatcher(R.id.rv_products)
}