package com.liso.pedidosnow.network

import android.content.Context
import com.liso.pedidosyatest.util.PedidosPreferences

import okhttp3.Interceptor
import okhttp3.Response

class CustomInterceptor(private val context: Context) : Interceptor {

    private val preferences: PedidosPreferences by lazy {
        PedidosPreferences(context)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
            .header("Authorization", preferences.getAccessToken())
            .method(originalRequest.method(), originalRequest.body())
            .build()

        return chain.proceed(request)
    }

}