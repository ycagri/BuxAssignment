package com.ycagri.buxassignment.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

open class CoroutineContexts(
    private val io: CoroutineDispatcher,
    private val default: CoroutineDispatcher,
    private val main: CoroutineDispatcher
) {

    @Inject
    constructor() : this(
        Dispatchers.IO,
        Dispatchers.Default,
        Dispatchers.Main
    )

    fun io(): CoroutineDispatcher {
        return io
    }

    fun default(): CoroutineDispatcher {
        return default
    }

    fun main(): CoroutineDispatcher {
        return main
    }
}