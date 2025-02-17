package com.mobilepiscine42.mediumweatherapp

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobilepiscine42.mediumweatherapp.geocoding_api.GeocodingViewModel
import com.mobilepiscine42.mediumweatherapp.geocoding_api.Result
import com.mobilepiscine42.mediumweatherapp.pageviewer.SharedViewModel
import com.mobilepiscine42.mediumweatherapp.pageviewer.ViewPagerAdapter
import com.mobilepiscine42.mediumweatherapp.reverse_geocoding_api.ReverseGeoViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private lateinit var weatherViewModel : WeatherViewModel
    private lateinit var geocodingViewModel : GeocodingViewModel
    private lateinit var reverseGeoViewModel : ReverseGeoViewModel
    private lateinit var sharedViewModel : SharedViewModel
    private lateinit var searchView : SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CitySuggestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setPageView()
        setLocationService()
        setSearchView()
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        geocodingViewModel = ViewModelProvider(this)[GeocodingViewModel::class.java]
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        reverseGeoViewModel = ViewModelProvider(this)[ReverseGeoViewModel::class.java]
        sharedViewModel.cityOptionsLiveData.observe(this) {
            Log.i("Cities", sharedViewModel.getCityOptions().toString())
            adapter.updateSuggestions(sharedViewModel.getCityOptions())
        }
    }

    private fun setPageView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager2)
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.currently -> viewPager.currentItem = 0
                R.id.today -> viewPager.currentItem = 1
                R.id.weekly -> viewPager.currentItem = 2
                else -> viewPager.currentItem = 0
            }
            true
        }
    }
    private fun setLocationService() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private fun setSearchView() {
        searchView = findViewById(R.id.searchGeoText)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CitySuggestionAdapter(mutableListOf()) { city -> onCitySelected(city) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    Log.i ("City name : ", query)
                    Log.e ("OnQueryTextSubmit", "${query}, log")
                    geocodingViewModel.getData(query, sharedViewModel)

                    searchView.setQuery("", false)
                    searchView.clearFocus()
                    recyclerView.visibility = View.GONE
                    val tmp = sharedViewModel.getCityOptions()
                    if (tmp.size != 0) {
                       onCitySelected(tmp[0])
                    } else {
                        sharedViewModel.setErrorMsg("Could not find any result for the provided address or coordinates.")
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == null || newText.length < 3)  {
                    Log.e ("OnQueryTextChange", "${newText}, log")
                    adapter.updateSuggestions(emptyList())
                    recyclerView.visibility = RecyclerView.GONE
                    return true
                }
                newText.let { query ->
                    if (query.isNotBlank()) {
                        recyclerView.visibility = RecyclerView.VISIBLE
                        geocodingViewModel.getData(query, sharedViewModel)
                    } else {
                        searchView.clearFocus()
                        adapter.updateSuggestions(emptyList())
                        recyclerView.visibility = View.GONE
                    }
                }
                return true
            }
        })

        searchView.setOnCloseListener {
            Log.e("onCLoseListener", "log")
            searchView.clearFocus()
            adapter.updateSuggestions(emptyList())
            recyclerView.visibility = View.GONE
            true
        }
    }

    private fun onCitySelected(city: Result) {
        weatherViewModel.getData(city.latitude.toString(), city.longitude.toString(), sharedViewModel, reverseGeoViewModel)
        searchView.setQuery("", false)
        searchView.clearFocus()
        sharedViewModel.setCityOptions(emptyList())
        recyclerView.visibility = View.GONE
        Log.i("RecycleView","Selected City: ${city.name}")
    }

    fun requestLocation(view : View) {
        if (checkLocationPermission()) {
            getGPS()
        } else {
            requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getGPS() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.i("GPS", "GPS is disabled! Please enable it in settings.")
            sharedViewModel.setErrorMsg("GPS is disabled.\nPlease turn it on in settings and try again.")
            return
        }

        locationManager.getCurrentLocation(
            LocationManager.GPS_PROVIDER,
            null,
            mainExecutor
        ) { location: Location? ->
            if (location != null) {
                val latitude = location.latitude.toString()
                val longitude = location.longitude.toString()
                weatherViewModel.getData(latitude, longitude, sharedViewModel, reverseGeoViewModel)
                Log.d("GPS", "latitude: $latitude, longitude: $longitude")
            } else {
                Log.i("GPS", "Error getting GPS location!")
                sharedViewModel.setErrorMsg("Cannot get location from GPS.\nPlease try again later.")
            }
        }
    }

    private fun checkLocationPermission() : Boolean {
        return ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            sharedViewModel.setErrorMsg("Location permission is not granted.\nPlease enable GPS access in the settings.")
            Log.i("Location permission", "Location permission denied")
        } else {
            getGPS()
        }
    }
}



