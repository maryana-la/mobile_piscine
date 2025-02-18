package com.mobilepiscine42.advanced_weather_app.pageviewer

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
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

        fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

        fun setupErrorMessage(errorMessage: TextView, mainLayout: LinearLayout) {
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
                bottomMargin = 60.dpToPx()
            }
            errorMessage.layoutParams = layoutParams
            errorMessage.setTextColor(Color.RED)
            errorMessage.textAlignment = TEXT_ALIGNMENT_CENTER
            errorMessage.textSize = 25f
            errorMessage.maxLines = 3
            errorMessage.ellipsize = TextUtils.TruncateAt.END
            errorMessage.setLineSpacing(4f, 1.2f)
            errorMessage.visibility = View.VISIBLE
            mainLayout.post {
                if (errorMessage.parent == null) {
                    Log.i("mainLayout?.post", errorMessage.text.toString())
                    mainLayout.addView(errorMessage, 3)
                } else {
                    Log.i("Error msg parent", errorMessage.parent.toString())
                }
            }
        }

        fun removeErrorMessage (errorMessage: TextView) {
            errorMessage.text = ""
            errorMessage.visibility = View.GONE
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