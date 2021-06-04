package com.ycagri.buxassignment.di

import com.ycagri.buxassignment.service.SubscriptionService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SubscriptionServiceModule {

    @ContributesAndroidInjector
    abstract fun provideSubscriptionService(): SubscriptionService
}