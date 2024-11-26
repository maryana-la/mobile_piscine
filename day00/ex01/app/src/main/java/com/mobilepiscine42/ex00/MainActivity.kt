package com.mobilepiscine42.ex00

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    val MAIN_LABEL = "Hello World!"
    val ALT_LABEL = "Cou-Cou :)"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val mainLabel: TextView = findViewById(R.id.main_label)
        var counter = false;

        button.setOnClickListener {
            Log.d("Button","Button pressed")
//            Toast.makeText(this, "Thanks for clicking", Toast.LENGTH_SHORT).show()

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