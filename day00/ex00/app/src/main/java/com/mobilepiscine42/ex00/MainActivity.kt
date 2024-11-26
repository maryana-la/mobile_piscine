package com.mobilepiscine42.ex00

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            Log.d("Button","Button pressed")
            Toast.makeText(this, "Thanks for clicking", Toast.LENGTH_SHORT).show()
        }

    }
}