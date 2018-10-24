package com.liso.pedidosyatest.network

import com.liso.pedidosyatest.BuildConfig
import com.liso.pedidosyatest.model.AccessToken
import com.liso.pedidosyatest.model.Restaurant
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("tokens")
    fun getAccessToken(@Query("clientId") clientId: String = BuildConfig.CLIENT_ID,
                       @Query("clientSecret") clientSecret: String = BuildConfig.CLIENT_SECRET) : Call<AccessToken>

    @GET("search/restaurants")
    fun searchRestaurants(@Query("point") location: String,
                          @Query("country") country: Int,
                          @Query("max")max: Int,
                          @Query("offset")offset: Int,
                          @Query("fields")fields: String = "name") : Call<Restaurant>
}