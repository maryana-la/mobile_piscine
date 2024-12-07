package com.mobilepiscine42.mobileweatherapp

import android.content.pm.PackageManager
import android.location.LocationRequest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobilepiscine42.mobileweatherapp.pageviewer.SharedViewModel
import com.mobilepiscine42.mobileweatherapp.pageviewer.ViewPagerAdapter
import com.mobilepiscine42.mobileweatherapp.api.Constant

class MainActivity : AppCompatActivity() {

    private var FINE_PERMISSION_CODE = 1

    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback


    private lateinit var weatherViewModel : WeatherViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI/UX page setup
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager2)
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // implement search of location
        weatherViewModel= ViewModelProvider(this)[WeatherViewModel::class.java]
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]


        weatherViewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })

        bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment = when (item.itemId) {
                R.id.currently -> viewPager.currentItem = 0
                R.id.today -> viewPager.currentItem = 1
                R.id.weekly -> viewPager.currentItem = 2
                else -> viewPager.currentItem = 0
            }
            true
        }


        val searchView = findViewById<SearchView>(R.id.searchGeoText)

// Handle query submission
// when the search text is submitted
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    Log.i ("City name : ", query)
                    displaySearchResult(it)
                    weatherViewModel.getData(query, sharedViewModel)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText != null) {
//                    weatherViewModel.getData(newText)
//                }
                return false
            }
        })
    }

    private fun displaySearchResult(query: String) {
        // Update fragments or views with the search result
//        Toast.makeText(this, "Searching for $query", Toast.LENGTH_SHORT).show()
    }

    fun getShareViewModel() : SharedViewModel {
        return sharedViewModel
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                Constant.REQUEST_CODE_LOCATION_PERMISSION
            )
            false
        } else {
            true
        }
    }

    fun requestLocation(view: View) {
        isLocationPermissionGranted()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    }
}



