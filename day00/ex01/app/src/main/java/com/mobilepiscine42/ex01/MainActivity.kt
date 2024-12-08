package com.mobilepiscine42.ex01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val MAIN_LABEL = "Hello World!"
    private val ALT_LABEL = "Cou-Cou :)"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val mainLabel: TextView = findViewById(R.id.main_label)
        var counter = false;

        button.setOnClickListener {
            Log.d("Button","Button pressed")

            if (counter == false) {
                mainLabel.text = ALT_LABEL
                counter = true
            }
            else {
                mainLabel.text = MAIN_LABEL
                counter = false
            }

        }

    }
}