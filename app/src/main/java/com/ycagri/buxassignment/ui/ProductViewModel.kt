package com.ycagri.buxassignment.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.ycagri.buxassignment.repository.ProductRepository
import com.ycagri.buxassignment.testing.OpenForTesting
import javax.inject.Inject

@OpenForTesting
class ProductViewModel @Inject constructor(val repository: ProductRepository) : ViewModel() {

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

    fun selectProduct(id: String) {
        if (id != selectedProduct.value)
            selectedProduct.postValue(id)
    }

    private fun getFormattedValue(precision: Int, v: Double, currency: String): String {
        val formatString = "%.${precision}f"
        return String.format("$formatString (%s)", v, currency)
    }
}