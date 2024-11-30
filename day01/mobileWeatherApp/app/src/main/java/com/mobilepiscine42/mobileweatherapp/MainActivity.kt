package com.mobilepiscine42.mobileweatherapp

import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var location: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    displaySearchResult(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optionally handle text changes in real-time
                return false
            }
        })

    }

    private fun displaySearchResult(query: String) {
        // Update fragments or views with the search result
        Toast.makeText(this, "Searching for $query", Toast.LENGTH_SHORT).show()
    }




}