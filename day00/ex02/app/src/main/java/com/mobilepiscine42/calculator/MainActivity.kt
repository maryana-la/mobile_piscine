package com.mobilepiscine42.calculator

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

//    " name_of_variable = findViewById<TextView>(R.id.name_of_textview) " (inside onCreate)
//    and than
//    " private la
//    Log.d("Button","Button pressed")

//    private lateinit var input: TextView
//    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        var header = findViewById<Toolbar>(R.id.header)
//        header.title = "Calculator"

//        var input = findViewById<TextView>(R.id.line1)
//        var result = findViewById<TextView>(R.id.line2)


    }

//    override fun onClick(v: View?) {
//
//        Log.d("button", "button pressed")
//    }

    fun buttonClicked(v: View) {
        Log.d("button", "button pressed : ${v.tag}")
    }



//    fun clickDebugMessage(view: View) {
//        if (view is Button) {
//             Log.d("button", "button pressed : $view.text")
//        }
//    }


//    fun numberAction(view: View) {
//        if (view is Button) {
//            input.append(view.text)
//        }
//    }

//    fun allClearAction(view: View) {
//        input.text = ""
//        result.text = ""
//    }

//    fun backSpaceAction(view: View) {
//        if (input.length() > 0) {
//            input.text = input.text.substring(0, input.length() - 1)
//        }
//    }

//    fun buttonClicked(view: View) {
//        Log.d("button", "button pressed : ${view.toString()}")
//    }


}