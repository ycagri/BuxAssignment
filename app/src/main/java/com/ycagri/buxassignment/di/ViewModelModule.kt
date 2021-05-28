package com.ycagri.buxassignment.di

import androidx.lifecycle.ViewModelProvider
import com.ycagri.buxassignment.util.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}