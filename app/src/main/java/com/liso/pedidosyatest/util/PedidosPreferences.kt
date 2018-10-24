package com.liso.pedidosyatest.util

import android.content.Context
import android.preference.PreferenceManager

class PedidosPreferences(private val context: Context) {

    private val sharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setAccessToken(accessToken: String) {
        setStringValue(KEY_ACCESS_TOKEN, accessToken)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getAccessToken(): String{
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, "")
    }

    private fun setStringValue(key: String, value: String) = sharedPreferences.edit().putString(key, value).apply()

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
    }
}