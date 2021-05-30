package com.ycagri.buxassignment.di

import com.ycagri.buxassignment.ui.ProductDetailFragment
import com.ycagri.buxassignment.ui.ProductListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsModule {

    @ContributesAndroidInjector
    abstract fun bindProductListFragment(): ProductListFragment

    @ContributesAndroidInjector
    abstract fun bindProductDetailFragment(): ProductDetailFragment
}