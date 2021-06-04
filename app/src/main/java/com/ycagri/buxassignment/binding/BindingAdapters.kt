package com.ycagri.buxassignment.binding

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("refreshing")
    fun isRefreshing(view: SwipeRefreshLayout, isRefreshing: Boolean) {
        view.isRefreshing = isRefreshing
    }

    @JvmStatic
    @BindingAdapter("refresh_callback")
    fun refreshCallback(view: SwipeRefreshLayout, listener: SwipeRefreshLayout.OnRefreshListener) {
        view.setOnRefreshListener(listener)
    }
}