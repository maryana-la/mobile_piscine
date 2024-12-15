package com.mobilepiscine42.mediumweatherapp

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobilepiscine42.mediumweatherapp.pageviewer.SharedViewModel
import com.mobilepiscine42.mediumweatherapp.pageviewer.ViewPagerAdapter
import com.mobilepiscine42.mediumweatherapp.api.Constant
import com.mobilepiscine42.mediumweatherapp.geocoding_api.GeocodingViewModel
import com.mobilepiscine42.mediumweatherapp.geocoding_api.Result

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var weatherViewModel : WeatherViewModel
    private lateinit var geocodingViewModel : GeocodingViewModel
    private lateinit var sharedViewModel: SharedViewModel


    private lateinit var recyclerView: RecyclerView
    private var cityOptionsFromApi = ArrayList<CitySuggestion>()
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 50000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(5000)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("Location Update", "Lat: $latitude, Lon: $longitude")
                }
            }
        }
    }

    private fun setSearchView() {
        val searchView = findViewById<SearchView>(R.id.searchGeoText)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CitySuggestionAdapter(emptyList()) { city -> onCitySelected(city) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    Log.i ("City name : ", query)
                    weatherViewModel.getData(query, "", sharedViewModel)
                }
                recyclerView.visibility = View.GONE
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.length!! < 3)  { return false }
                val citySuggestion : List<Result> = geocodingViewModel.getData(newText)
                if (citySuggestion.isNotEmpty()) {
                    recyclerView.visibility = View.VISIBLE
                    adapter.updateSuggestions(adaptCityList(citySuggestion))
                } else {
                    recyclerView.visibility = View.GONE
                }
                return true
            }
        })
    }


    private fun adaptCityList (fromApi : List<Result>) : List<CitySuggestion> {
        val returnValue : MutableList<CitySuggestion> = mutableListOf()
        for (item in fromApi) {
            val tmp = CitySuggestion(item.name, item.admin1, item.country)
            returnValue.add(tmp)
        }
        return returnValue
    }

    private fun onCitySelected(city: CitySuggestion) {
        recyclerView.visibility = View.GONE
        // Handle city selection (e.g., fetch weather data for this city)
        println("Selected City: ${city.name}")
    }



    fun requestLocation(view: View) {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!isLocationPermissionGranted()) {
                Toast.makeText(this, "GeoLocation is not available. Please enable GPS in the settings.", Toast.LENGTH_SHORT).show()
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude.toString()
                val longitude = location.longitude.toString()
                weatherViewModel.getData(latitude, longitude, sharedViewModel)
                Log.d("Location", "Lat: $latitude, Lon: $longitude")
            } else {
                Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Log.e("Location", "Error fetching location", e)
        }
    }

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

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}



