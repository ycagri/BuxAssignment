package com.ycagri.buxassignment.di

import com.ycagri.buxassignment.ui.ProductActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProductActivityModule {

    @ContributesAndroidInjector(modules = [FragmentsModule::class])
    abstract fun bindProductActivity(): ProductActivity
}