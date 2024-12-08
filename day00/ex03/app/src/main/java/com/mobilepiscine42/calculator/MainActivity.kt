package com.mobilepiscine42.calculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder
import java.math.MathContext.DECIMAL64
import java.math.BigDecimal


class MainActivity : AppCompatActivity() {

    private lateinit var input: TextView
    private lateinit var result: TextView
    private lateinit var horizontalScrollView: HorizontalScrollView
    private var canAddSign = false
    private var endOfCalculation = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.line1)
        result = findViewById(R.id.line2)
        horizontalScrollView = findViewById(R.id.horizontalScrollView)

        if (savedInstanceState != null) {
            input.text = savedInstanceState.getString("INPUT_TEXT", "")
            result.text = savedInstanceState.getString("RESULT_TEXT", "")
            canAddSign = savedInstanceState.getBoolean("CAN_ADD_SIGN", false)
            endOfCalculation = savedInstanceState.getBoolean("END_OF_CALCULATION", false)
        }

        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                horizontalScrollView.post {
                    horizontalScrollView.fullScroll(View.FOCUS_RIGHT)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("INPUT_TEXT", input.text.toString())
        outState.putString("RESULT_TEXT", result.text.toString())
        outState.putBoolean("CAN_ADD_SIGN", canAddSign)
        outState.putBoolean("END_OF_CALCULATION", endOfCalculation)
    }

    private fun buttonClicked(button: Button) {
        Log.d("button", "button pressed: ${button.text}")
    }

    fun numberClicked(view: View) {
        if (view is Button) {
            buttonClicked(view)
            checkEndOfCalculation()
            result.text= ""
            if (view.text == ".") {
                appendDotOperator(view)
                canAddSign = false
            }
            else {
                val buttonText = (view as TextView).text
                if (input.text.isEmpty() && buttonText.startsWith('0')) { return }
                input.text = "${input.text}$buttonText"
                canAddSign = true
            }
        }
    }

    fun letterClicked(view: View) {
        if (view is Button) {
            buttonClicked(view)
            checkEndOfCalculation()
            when (view.text) {
                "C" -> backSpaceAction(view)
                "AC" -> allClearAction()
            }
        }
    }

    fun operatorClicked(view: View) {
        if (view is Button) {
            buttonClicked(view)
            checkEndOfCalculation()
            when(view.text) {
                "-" -> appendMinusOperator(view)
                "+" -> appendOperator(view)
                "*" -> appendOperator(view)
                "/" -> appendOperator(view)
                "=" -> {
                    result.text = calculateExpression()
                }
            }
        }
    }

    private fun calculateExpression(): String {
        if (input.text.isEmpty())
            return ""
        endOfCalculation = true
        return try {
            val e = ExpressionBuilder(input.text.toString()).build()
            val res = e.evaluate()
            val decimal = BigDecimal(res, DECIMAL64)
            if (decimal.scale() <= 0) {
                decimal.toString()
            } else {
                decimal.stripTrailingZeros().toString()
            }
        } catch (ex: Exception) {
            ex.localizedMessage ?:"Error"
        }
    }

    private fun appendDotOperator(button: Button) {
        if (!input.text.contains('.')) {
            input.append(".")
        }
        else{
            val len = input.text.length
            var dotIndex = input.text.lastIndexOf('.', len, true)
            var operatorIndex = input.text.lastIndexOfAny("+-*/".toCharArray(), len, true)
            if ( operatorIndex > 0 && dotIndex <= operatorIndex ) {
                input.append(".")
            }
        }
    }

    private fun allClearAction() {
        input.text = ""
        result.text = ""
    }

    private fun checkEndOfCalculation() {
        if (endOfCalculation) {
            allClearAction()
            endOfCalculation = false
        }
    }

    private fun backSpaceAction(view: View) {
        if (input.length() > 1) {
            input.text = input.text.substring(0, input.length() - 1)
            if (input.text.last().isDigit()) { canAddSign = true }
        } else {
            input.text = ""
        }
    }

    private fun appendOperator(button: Button) {
        if (input.text.isEmpty() || (input.text.length == 1 && input.text.startsWith('-')))
            return
        val ch = input.text.last()
        if (canAddSign) {
            input.append(button.text)
            canAddSign = false
        } else {
            if (input.text.endsWith("/-") || input.text.endsWith("*-")) {
                input.text = "${input.text.dropLast(2)}${button.text}"
            } else if (ch != '.') {
                input.text = "${input.text.dropLast(1)}${button.text}"
            }
        }
    }

    private fun appendMinusOperator(button: Button) {
        if (canAddSign || input.text.isEmpty()) {
            input.append(button.text)
            canAddSign = false
        } else {
            when (input.text.last()) {
                '*' -> input.append(button.text)
                '/' -> input.append(button.text)
                '+' -> input.text = "${input.text.dropLast(1)}${button.text}"
                '-' -> {
                    if (input.text.length > 2 && !input.text[input.text.length - 2].isDigit()) {
                        input.text = "${input.text.dropLast(2)}${button.text}"
                        canAddSign = false
                    }
                }
            }
        }
    }

}