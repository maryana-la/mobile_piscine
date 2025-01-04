package com.mobilepiscine42.mediumweatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobilepiscine42.mediumweatherapp.geocoding_api.Result

class CitySuggestionAdapter(
    private var suggestions: List<Result>,
    private val onCityClicked: (Result) -> Unit
) : RecyclerView.Adapter<CitySuggestionAdapter.CityViewHolder>() {

    fun updateSuggestions(newSuggestions: List<Result>) {
        suggestions = newSuggestions
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city_suggestion, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = suggestions[position]
        holder.bind(city)
        holder.itemView.setOnClickListener { onCityClicked(city) }
    }

    override fun getItemCount(): Int {
        return suggestions.size
    }

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cityName: TextView = itemView.findViewById(R.id.cityOption)

        fun bind(city: Result) {
            cityName.text = listOfNotNull(city.name, city.admin1, city.country)
                .joinToString(separator = ", ")
        }
    }
}
