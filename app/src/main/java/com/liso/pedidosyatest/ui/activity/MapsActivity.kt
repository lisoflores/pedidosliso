package com.liso.pedidosyatest.ui.activity

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.liso.pedidosyatest.R

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        lastLocation = intent.getParcelableExtra(EXTRA_LAST_LOCATION)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.addMarker(MarkerOptions().position(lastLocation).title(getString(R.string.last_location_selected)))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLocation))
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_view_map -> {
                returnNewLocation()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun returnNewLocation() {
        val intent = Intent()
        intent.putExtra(MainActivity.EXTRA_NEW_LOCATION, mMap.cameraPosition.target)
        setResult(Activity.RESULT_OK, intent)
        this.finish()
    }

    companion object {
        const val EXTRA_LAST_LOCATION = "extra_last_location"
    }
}
