package com.mobilepiscine42.mediumweatherapp.pageviewer

import android.content.Context
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.widget.LinearLayout
import android.widget.TextView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Util {

    companion object {

        fun getWindDirection(angle: Int): String {
            val directions = listOf("↓ N", "↙ NE", "← E", "↖ SE", "↑ S", "↗ SW", "→ W", "↘ NW")
            return directions[(Math.round(angle.toDouble() / 45) % 8).toInt()];
        }

        fun formatTimeHHMM(input: String): String {
            val dateTime = LocalDateTime.parse(input)
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            return dateTime.format(formatter)
        }

        fun formatDate(input: String): String {
            val dateTime = LocalDateTime.parse(input)
            val formatter = DateTimeFormatter.ofPattern("dd mmm yyyy")
            return dateTime.format(formatter)
        }

        fun setTextViewForFragments(input : String, context : Context) : TextView {
            val result = TextView(context).apply {
                text = input
                textSize = 20f
                textAlignment = TEXT_ALIGNMENT_CENTER
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f)
            }
            return result
        }

        fun weatherCode(code: Int): String = when (code) {
            0 -> "Clear sky"
            1, 2, 3 -> "Overcast"
            45, 48 -> "Fog"
            51 -> "Light drizzle"
            53 -> "Moderate drizzle"
            55 -> "Intense drizzle"
            56 -> "Light freezing drizzle"
            57 -> "Intense freezing drizzle"
            61 -> "Slight rain"
            63 -> "Moderate rain"
            65 -> "Heavy rain"
            66, 67 -> "Freezing rain"
            71, 73, 75 -> "Snow fall"
            77 -> "Snow grains"
            80, 81, 82 -> "Rain showers"
            85, 86 -> "Snow showers"
            95 -> "Thunderstorm"
            96, 99 -> "Thunderstorm with hail"
            else -> "Not defined"
        }

    }
}