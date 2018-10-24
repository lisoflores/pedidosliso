package com.liso.pedidosyatest.network

import android.content.Context
import com.liso.pedidosnow.network.CustomInterceptor
import com.liso.pedidosyatest.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    private var service: ApiInterface? = null

    fun getService(context: Context) : ApiInterface {
        if (service == null) {
            service = prepare(context)
        }

        return service!!
    }

    private fun prepare(context: Context) : ApiInterface {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addNetworkInterceptor(CustomInterceptor(context))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiInterface::class.java)
    }
}