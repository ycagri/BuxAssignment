package com.ycagri.buxassignment.ui

import androidx.lifecycle.ViewModel
import com.ycagri.buxassignment.repository.ProductRepository
import com.ycagri.buxassignment.testing.OpenForTesting
import javax.inject.Inject

@OpenForTesting
class ProductViewModel @Inject constructor(val repository: ProductRepository): ViewModel() {
}