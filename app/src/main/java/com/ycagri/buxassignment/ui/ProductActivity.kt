package com.ycagri.buxassignment.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ycagri.buxassignment.R
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class ProductActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val productViewModel: ProductViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        setSupportActionBar(findViewById(R.id.toolbar))

        productViewModel.selectedProduct.observe(this) {
            val container = findViewById<View>(R.id.container_detail)
            if (container == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container_master, ProductDetailFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}