package com.ycagri.buxassignment.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ycagri.buxassignment.db.ProductSubscriptionEntity
import com.ycagri.buxassignment.repository.ProductRepository
import com.ycagri.buxassignment.testing.OpenForTesting
import com.ycagri.buxassignment.util.CoroutineContexts
import kotlinx.coroutines.launch
import javax.inject.Inject

@OpenForTesting
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val contexts: CoroutineContexts
) : ViewModel() {

    val products by lazy { repository.getProducts() }

    val selectedProduct = MutableLiveData<String>()

    val product by lazy {
        switchMap(selectedProduct) {
            repository.getProduct(it)
        }
    }

    private val _currentPrice by lazy {
        switchMap(selectedProduct) {
            repository.getCurrentPrice(it)
        }
    }

    val currentPrice = map(_currentPrice) {
        getFormattedValue(it.decimals, it.amount, it.currency)
    }

    private val _closingPrice by lazy {
        switchMap(selectedProduct) {
            repository.getClosingPrice(it)
        }
    }

    val closingPrice = map(_closingPrice) {
        getFormattedValue(it.decimals, it.amount, it.currency)
    }

    private val _dayRange by lazy {
        switchMap(selectedProduct) {
            repository.getDayRange(it)
        }
    }

    private val _yearRange by lazy {
        switchMap(selectedProduct) {
            repository.getYearRange(it)
        }
    }

    val dayRangeLow = map(_dayRange) {
        getFormattedValue(it.decimals, it.low, it.currency)
    }

    val dayRangeHigh = map(_dayRange) {
        getFormattedValue(it.decimals, it.high, it.currency)
    }

    val yearRangeLow = map(_yearRange) {
        getFormattedValue(it.decimals, it.low, it.currency)
    }

    val yearRangeHigh = map(_yearRange) {
        getFormattedValue(it.decimals, it.high, it.currency)
    }

    private val subscription by lazy {
        switchMap(selectedProduct) {
            repository.getSubscription(it)
        }
    }

    val subscribed = map(subscription) {
        it?.subscribed ?: 0
    }


    fun selectProduct(id: String) {
        if (id != selectedProduct.value)
            selectedProduct.postValue(id)
    }

    fun subscribeProduct() {
        viewModelScope.launch(contexts.io()) {
            if (subscription.value == null) {
                product.value?.data?.let {
                    repository.insertSubscription(
                        ProductSubscriptionEntity(
                            id = 0,
                            productId = it.securityId,
                            subscribed = 1
                        )
                    )
                }
            } else {
                subscription.value?.let {
                    val s = if (it.subscribed == 0) 1 else 0
                    repository.updateSubscription(
                        ProductSubscriptionEntity(
                            it.id,
                            it.productId,
                            s
                        )
                    )
                }

            }
        }
    }

    private fun getFormattedValue(precision: Int, v: Double, currency: String): String {
        val formatString = "%.${precision}f"
        return String.format("$formatString (%s)", v, currency)
    }
}