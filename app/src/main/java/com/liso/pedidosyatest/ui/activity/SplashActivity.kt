package com.liso.pedidosyatest.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.liso.pedidosyatest.R
import com.liso.pedidosyatest.model.AccessToken
import com.liso.pedidosyatest.network.ApiService
import com.liso.pedidosyatest.util.PedidosPreferences
import com.liso.pedidosyatest.util.toGone
import kotlinx.android.synthetic.main.activity_splash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    private lateinit var preferences: PedidosPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        preferences = PedidosPreferences(this)

        val call = ApiService.getService(this).getAccessToken()
        call.enqueue(object: Callback<AccessToken> {
            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                pb_splash.toGone()
                Toast.makeText(this@SplashActivity, getString(R.string.error_message), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                pb_splash.toGone()
                response.body()?.let {
                    preferences.setAccessToken(it.accessToken)
                    openMainActivity()
                } ?: run {
                    Toast.makeText(this@SplashActivity, getString(R.string.error_message), Toast.LENGTH_LONG).show()
                }


            }

        })
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

}