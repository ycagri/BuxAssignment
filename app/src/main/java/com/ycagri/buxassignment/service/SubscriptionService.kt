package com.ycagri.buxassignment.service

import android.app.Service
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.google.gson.Gson
import com.ycagri.buxassignment.api.ChannelUpdate
import com.ycagri.buxassignment.api.Subscription
import com.ycagri.buxassignment.repository.ProductRepository
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasServiceInjector
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class SubscriptionService :
    LifecycleService(), HasServiceInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var repository: ProductRepository

    private val _socketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d("Subscription", text)
            val update = Gson().fromJson(text, ChannelUpdate::class.java)
            update.body.securityId?.let { id ->
                update.body.currentPrice?.let { price ->
                    repository.updateCurrentPrice(
                        id,
                        price.toDouble()
                    )
                }
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
        }
    }

    private val _subscriptions by lazy { repository.getSubscriptions() }

    private val socket by lazy { repository.getSubscriptionConnection(_socketListener) }

    private val _productMap = HashMap<String, Int>()

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        _subscriptions.observe(this) {
            val subscription = Subscription(ArrayList(), ArrayList())
            it.forEach { p ->
                val s = _productMap[p.productId]
                if (s == null) {
                    _productMap[p.productId] = p.subscribed
                } else {
                    if (s != p.subscribed) {
                        if (p.subscribed == 1) {
                            subscription.subscribeList.add("trading.product.${p.productId}")
                        } else {
                            subscription.unsubscribeList.add("trading.product.${p.productId}")
                        }
                        _productMap[p.productId] = p.subscribed
                    }
                }
            }

            socket.send(Gson().toJson(subscription))
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun serviceInjector() = injector
}