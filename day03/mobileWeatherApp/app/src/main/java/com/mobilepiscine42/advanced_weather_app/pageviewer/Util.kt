package com.mobilepiscine42.advanced_weather_app.pageviewer

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.mobilepiscine42.advanced_weather_app.R
import com.mobilepiscine42.advanced_weather_app.api.Constant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class Util {

    companion object {

        fun getWindDirection(angle: Int): String {
            val directions = listOf("↓", "↙", "←", "↖", "↑", "↗", "→", "↘")
            return directions[(Math.round(angle.toDouble() / 45) % 8).toInt()];
        }

        fun formatTimeHHMM(input: String): String {
            val dateTime = LocalDateTime.parse(input)
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            return dateTime.format(formatter)
        }

        fun formatDate(input: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
                val date = inputFormat.parse(input)
                date?.let { outputFormat.format(it) } ?: ""
            } catch (e: Exception) {
                ""
            }
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

        fun setupErrorMessageConstraint(errorMessage: TextView, mainLayout: ConstraintLayout) {
//            val layoutParams = ConstraintLayout.LayoutParams().apply {
//                gravity = Gravity.CENTER
//                bottomMargin = 60.dpToPx()
//            }
//            errorMessage.layoutParams = layoutParams
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

        fun setWeatherDescription(code: Int): String = when (code) {
            0 -> "Clear sky"
            1, 2, 3 -> "Partly cloudy"
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
            else -> ""
        }

        fun setWeatherImage(context: Context, weatherCode: Int): Drawable? {
            val drawableResId = when (weatherCode) {
                0 -> R.drawable.ic_weather_sunny
                1, 2, 3 -> R.drawable.ic_weather_partly_cloudy
                45, 48 -> R.drawable.ic_weather_fog
                51, 53, 55, 56, 57 -> R.drawable.ic_weather_drizzle
                61, 63 -> R.drawable.ic_weather_light_rain
                65, 66, 67, 80, 81, 82 -> R.drawable.ic_weather_heavy_rain
                71, 73, 75, 77, 85, 86 -> R.drawable.ic_weather_snow
                95 -> R.drawable.ic_weather_thunderstorm
                96, 99 -> R.drawable.ic_weather_hail
                else -> R.drawable.ic_weather_undefined
            }

            return ContextCompat.getDrawable(context, drawableResId)
        }

        fun resizeVectorDrawable(context: Context, drawableId: Int, width: Int, height: Int): BitmapDrawable {
            val drawable = ContextCompat.getDrawable(context, drawableId) ?: return BitmapDrawable()

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            return BitmapDrawable(context.resources, bitmap)
        }

        fun createChartForHourlyForecast(sharedViewModel: SharedViewModel, lineChart: LineChart) {
            val entries = ArrayList<Entry>()

            val hourlyTemp = sharedViewModel.getWeatherForecast().hourly.temperature_2m

            for (i in 0 until Constant.QUANTITY_HOURS_FOR_TODAY_FRAGMENT) {
                entries.add(Entry(i.toFloat(), hourlyTemp[i].toFloat()))
            }

            val dataSet = LineDataSet(entries, "Temperature")

            dataSet.setDrawValues(false)
            dataSet.setDrawFilled(false)
            dataSet.setDrawCircles(true)
            dataSet.setCircleColor(Color.rgb(16,236,159))
            dataSet.circleRadius = 5f
            dataSet.circleHoleColor = Color.WHITE
            dataSet.color = Color.rgb(16,236,159)
            dataSet.valueTextSize = 10f
            dataSet.lineWidth = 2f

            lineChart.data = LineData(dataSet)

            //set Axis
            setLineChartAxis(lineChart)
            lineChart.legend?.isEnabled = false
            lineChart.setExtraOffsets(0f, 0f, 5f, 10f)
        }


        fun createChartForDailyForecast(sharedViewModel: SharedViewModel, lineChart: LineChart) {
            setLineChartAxis(lineChart)
            val dateLabels = ArrayList<String>()
            val daily = sharedViewModel.getWeatherForecast().daily

            val minTemperatureLine = ArrayList<Entry>()
            for(i in 0 until Constant.FORECAST_DAYS) {
                minTemperatureLine.add(Entry(i.toFloat(), daily.temperature_2m_min[i].toFloat()))
                dateLabels.add(formatDate(daily.time[i]))
            }

            val maxTemperatureLine = ArrayList<Entry>()
            for(i in 0 until Constant.FORECAST_DAYS) {
                maxTemperatureLine.add(Entry(i.toFloat(), daily.temperature_2m_max[i].toFloat()))
            }


            val minDataSet = LineDataSet(minTemperatureLine, "Min Temperature")
            minDataSet.setDrawValues(false)
            minDataSet.setDrawFilled(false)
            minDataSet.setDrawCircles(true)
            minDataSet.setCircleColor(Color.rgb(33,150,243))
            minDataSet.circleRadius = 5f
            minDataSet.circleHoleColor = Color.WHITE
            minDataSet.color = Color.rgb(33,150,243)
            minDataSet.valueTextSize = 10f
            minDataSet.lineWidth = 2f

            val maxDataSet = LineDataSet(maxTemperatureLine, "Max Temperature")
            maxDataSet.setDrawValues(false)
            maxDataSet.setDrawFilled(false)
            maxDataSet.setDrawCircles(true)
            maxDataSet.setCircleColor(Color.RED)
            maxDataSet.circleRadius = 5f
            maxDataSet.circleHoleColor = Color.WHITE
            maxDataSet.color = Color.RED
            maxDataSet.valueTextSize = 10f
            maxDataSet.lineWidth = 2f


            lineChart.data = LineData(minDataSet, maxDataSet)
            lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(dateLabels)
            lineChart.xAxis.granularity = 1f
            lineChart.xAxis.setLabelCount(dateLabels.size, true)




            lineChart.legend?.textColor = Color.WHITE
            lineChart.legend?.textSize = 12f
            lineChart.legend?.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            lineChart.legend?.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            lineChart.setExtraOffsets(0f, 0f, 5f, 15f)

        }

        private fun setLineChartAxis(lineChart: LineChart) {
            lineChart.xAxis?.position = XAxis.XAxisPosition.BOTTOM
            lineChart.xAxis?.textColor = Color.WHITE
            lineChart.xAxis?.valueFormatter = TimeAxisValueFormatter()
            lineChart.xAxis?.setDrawGridLines(true)

            lineChart.axisLeft.textColor = Color.WHITE
            lineChart.axisLeft.setDrawGridLines(true)

            lineChart.axisRight?.isEnabled = false
            lineChart.description.isEnabled = false
            lineChart.invalidate()
            lineChart.visibility = View.VISIBLE

            lineChart.setTouchEnabled(true)
            lineChart.setPinchZoom(true)

            lineChart.animateX(1800, Easing.EaseInExpo)
        }
    }
}