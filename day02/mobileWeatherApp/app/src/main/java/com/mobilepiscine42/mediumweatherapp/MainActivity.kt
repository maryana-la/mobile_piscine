package com.mobilepiscine42.mediumweatherapp

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobilepiscine42.mediumweatherapp.api.Constant
import com.mobilepiscine42.mediumweatherapp.geocoding_api.GeocodingViewModel
import com.mobilepiscine42.mediumweatherapp.geocoding_api.Result
import com.mobilepiscine42.mediumweatherapp.pageviewer.SharedViewModel
import com.mobilepiscine42.mediumweatherapp.pageviewer.ViewPagerAdapter
import com.mobilepiscine42.mediumweatherapp.reverse_geocoding_api.ReverseGeoViewModel


class MainActivity : AppCompatActivity() {

//    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
//    private lateinit var locationRequest: LocationRequest
//    private lateinit var locationCallback: LocationCallback
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
        weatherViewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
                    geocodingViewModel.getData(query, sharedViewModel)
                    if (sharedViewModel.getCityOptions().isNotEmpty()) {
                        weatherViewModel.getData(
                            sharedViewModel.getCityOptions()[0].latitude.toString(),
                            sharedViewModel.getCityOptions()[0].longitude.toString(),
                            sharedViewModel,
                            reverseGeoViewModel
                        )
                    } else {
                        sharedViewModel.setErrorMsg("Connection failure.")
                    }
                    query.removeRange(0, query.length)
                        //TODO : add erorr - no network
                }
                recyclerView.visibility = View.GONE
                searchView.setQuery("", false)
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.length!! < 3)  {
                    recyclerView.visibility = RecyclerView.VISIBLE
                    adapter.updateSuggestions(emptyList())
                    return false
                }
                newText.let { query ->
                    if (query.isNotBlank()) {
                        geocodingViewModel.getData(query, sharedViewModel)
                        adapter.updateSuggestions(sharedViewModel.getCityOptions())
                        recyclerView.visibility = RecyclerView.VISIBLE
                    } else {
                        recyclerView.visibility = RecyclerView.GONE
                    }
                }
                return true
            }
        })

        searchView.setOnCloseListener {
            adapter.updateSuggestions(emptyList())
            recyclerView.visibility = RecyclerView.GONE
            true
        }

    }

    private fun onCitySelected(city: Result) {
        weatherViewModel.getData(city.latitude.toString(), city.longitude.toString(), sharedViewModel, reverseGeoViewModel)
        searchView.setQuery("", false)
        searchView.clearFocus()
        recyclerView.visibility = View.GONE
        Log.i("RecycleView","Selected City: ${city.name}")
        Toast.makeText(this, "Selected City: ${city.name}", Toast.LENGTH_SHORT).show()
    }


    fun requestLocation(view: View) {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!isLocationPermissionGranted()) {
                sharedViewModel.setErrorMsg("GeoLocation is not available. Please enable GPS in the settings.")
            }
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e("GPS", "GPS is disabled! Please enable it in settings.")
            sharedViewModel.setErrorMsg("GPS is disabled! Please enable it in settings.")
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
                Log.e("GPS", "Error getting GPS location!")
                sharedViewModel.setErrorMsg("Network error.")
            }
        }
    }

//    fun requestLocation(view: View) {
//        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            if (!isLocationPermissionGranted()) {
//                sharedViewModel.setErrorMsg("GeoLocation is not available. Please enable GPS in the settings.")
//            }
//        }
//
//        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
//        val task: Task<Location> = fusedLocationProviderClient.
//            getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token)
//        task.addOnSuccessListener { location ->
//            if (location != null) {
//                val latitude = location.latitude.toString()
//                val longitude = location.longitude.toString()
//                weatherViewModel.getData(latitude, longitude, sharedViewModel)
//                Log.d("Location", "Lat: $latitude, Lon: $longitude")
//            } else {
//                sharedViewModel.setErrorMsg("Network error.")
//            }
//        }.addOnFailureListener { e ->
//            Log.e("Location", "Error fetching location", e)
//            sharedViewModel.setErrorMsg("Error fetching location.")
//        }
//    }



    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), Constant.REQUEST_CODE_LOCATION_PERMISSION)
            false
        } else {
            true
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
//    }
}



