package com.liso.pedidosyatest.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.liso.pedidosyatest.R
import com.liso.pedidosyatest.model.Restaurant
import com.liso.pedidosyatest.network.ApiService
import com.liso.pedidosyatest.ui.adapter.RestaurantAdapter
import com.liso.pedidosyatest.util.toGone
import com.liso.pedidosyatest.util.toVisible
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var adapter: RestaurantAdapter? = null
    private var lastLocation = LatLng(-34.90359, -56.19264)
    private var isLoading = true
    private var offset = 0
    private val maxItems = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        getRestaurants(lastLocation, COUNTRY)
    }

    private fun init() {
        rv_main?.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_main?.layoutManager = layoutManager

        rv_main?.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lManager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                val totalItemCount = lManager!!.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                val endHasBeenReached = lastVisible + 5 >= totalItemCount
                if (totalItemCount > 0 && endHasBeenReached) {
                    if(!isLoading) {
                        isLoading = true
                        getRestaurants(lastLocation, COUNTRY, true)
                    }
                }
            }
        })
    }

    private fun getRestaurants(location: LatLng, country: Int, shouldAdd: Boolean = false) {

        pb_main?.toVisible()

        val locationText = "${location.latitude},${location.longitude}"

        val call = ApiService.getService(this).searchRestaurants(locationText, country, maxItems, offset)
        call.enqueue(object: Callback<Restaurant> {
            override fun onFailure(call: Call<Restaurant>, t: Throwable) {
                pb_main?.toGone()
                Toast.makeText(this@MainActivity, getString(R.string.error_message), Toast.LENGTH_LONG).show()
                isLoading = false
            }

            override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
                pb_main?.toGone()
                isLoading = false

                response.body()?.let {
                    offset += maxItems
                    setRestaurants(it, shouldAdd)
                }
            }
        })
    }

    private fun setRestaurants(restaurants: Restaurant, shouldAdd: Boolean = false){

        if (restaurants.total == 0) {
            Toast.makeText(this, getString(R.string.restaurants_not_found), Toast.LENGTH_LONG).show()
        }

        if (adapter == null) {
            adapter = RestaurantAdapter(restaurants)
            rv_main?.adapter = adapter
        } else {
            if (shouldAdd) {
                adapter?.addRestaurants(restaurants)
            } else {
                adapter?.updateRestaurants(restaurants)
            }

        }
    }

    private fun openMapsActivity() {
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra(MapsActivity.EXTRA_LAST_LOCATION, lastLocation)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val newLocation: LatLng? = data?.getParcelableExtra(EXTRA_NEW_LOCATION)

            newLocation?.let {
                offset = 0
                isLoading = false
                lastLocation = it
                getRestaurants(it, COUNTRY)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_view_map -> {
                openMapsActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val COUNTRY = 1
        const val REQUEST_CODE = 100
        const val EXTRA_NEW_LOCATION = "extra_new_location"
    }

}