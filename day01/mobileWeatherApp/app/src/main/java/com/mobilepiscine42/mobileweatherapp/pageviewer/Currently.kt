package com.mobilepiscine42.mobileweatherapp.pageviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mobilepiscine42.mobileweatherapp.R

class Currently : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_currently, container, false)
        val locationText = view?.findViewById<TextView>(R.id.location_text)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        // Observe location updates
        sharedViewModel.location.observe(viewLifecycleOwner) {
            locationText?.text = sharedViewModel.getCurrentLocation()
        }
        return view
    }
}