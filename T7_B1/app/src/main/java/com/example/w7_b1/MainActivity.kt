package com.example.w7_b1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {

    private lateinit var dispNum: TextView
    private lateinit var numberButtons: List<Button>
    private lateinit var btnC: Button
    private lateinit var btnEqual: Button
    private lateinit var btnPlus: Button
    private lateinit var btnMinus: Button
    private lateinit var btnMul: Button
    private lateinit var btnDiv: Button
    private lateinit var btnDot: Button
    private lateinit var btnCE: Button
    private lateinit var btnBS: Button
    private lateinit var btnSign: Button

    private var currentNumber = ""
    private var previousNumber = ""
    private var operator = ""
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.caculator_layout)

        initializeViews()
        setupListeners()
        dispNum.text = "0"
    }

    private fun initializeViews() {
        dispNum = findViewById(R.id.dispNum)

        numberButtons = listOf(
            findViewById(R.id.btn0),
            findViewById(R.id.btn1),
            findViewById(R.id.btn2),
            findViewById(R.id.btn3),
            findViewById(R.id.btn4),
            findViewById(R.id.btn5),
            findViewById(R.id.btn6),
            findViewById(R.id.btn7),
            findViewById(R.id.btn8),
            findViewById(R.id.btn9)
        )

        btnC = findViewById(R.id.btnC)
        btnCE = findViewById(R.id.btnCE)
        btnBS = findViewById(R.id.btnBS)
        btnEqual = findViewById(R.id.btnEqual)
        btnPlus = findViewById(R.id.btnAdd)
        btnMinus = findViewById(R.id.btnSub)
        btnMul = findViewById(R.id.btnMult)
        btnDiv = findViewById(R.id.btnDiv)
        btnDot = findViewById(R.id.btnDot)
        btnSign = findViewById(R.id.btnSign)
    }

    private fun setupListeners() {
        val numberClickListener = View.OnClickListener { view ->
            onNumberClick((view as Button).text.toString())
        }

        numberButtons.forEach { it.setOnClickListener(numberClickListener) }

        btnPlus.setOnClickListener { onOperatorClick("+") }
        btnMinus.setOnClickListener { onOperatorClick("-") }
        btnMul.setOnClickListener { onOperatorClick("×") }
        btnDiv.setOnClickListener { onOperatorClick("/") }

        btnEqual.setOnClickListener { onEqualClick() }
        btnC.setOnClickListener { onClearClick() }
        btnCE.setOnClickListener { onClearEntryClick() }
        btnBS.setOnClickListener { onBackspaceClick() }
        btnDot.setOnClickListener { onDotClick() }
        btnSign.setOnClickListener { onSignClick() }
    }

    private fun onNumberClick(number: String) {
        currentNumber = if (isNewOperation) {
            isNewOperation = false
            number
        } else {
            currentNumber + number
        }
        updateDisplay()
    }

    private fun onOperatorClick(op: String) {
        when {
            currentNumber.isNotEmpty() -> {
                if (previousNumber.isNotEmpty() && operator.isNotEmpty() && !isNewOperation) {
                    calculateResult()
                }
                previousNumber = currentNumber
                operator = op
                isNewOperation = true
            }
            previousNumber.isNotEmpty() -> operator = op
        }
    }

    private fun onEqualClick() {
        if (previousNumber.isNotEmpty() && currentNumber.isNotEmpty() && operator.isNotEmpty()) {
            calculateResult()
            operator = ""
            previousNumber = ""
        }
    }

    private fun calculateResult() {
        try {
            val num1 = previousNumber.toDouble()
            val num2 = currentNumber.toDouble()

            val result = when (operator) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "×" -> num1 * num2
                "/" -> {
                    if (num2 == 0.0) {
                        showError()
                        return
                    }
                    num1 / num2
                }
                else -> return
            }

            currentNumber = if (result % 1.0 == 0.0) {
                result.toLong().toString()
            } else {
                result.toString()
            }
            isNewOperation = true
            updateDisplay()
        } catch (e: Exception) {
            showError()
        }
    }

    private fun showError() {
        dispNum.text = "Error"
        resetState()
    }

    private fun resetState() {
        currentNumber = ""
        previousNumber = ""
        operator = ""
        isNewOperation = true
    }

    private fun onClearClick() {
        resetState()
        dispNum.text = "0"
    }

    private fun onClearEntryClick() {
        currentNumber = ""
        isNewOperation = true
        dispNum.text = "0"
    }

    private fun onBackspaceClick() {
        if (currentNumber.isNotEmpty() && !isNewOperation) {
            currentNumber = currentNumber.dropLast(1)
            updateDisplay()
        }
    }

    private fun onDotClick() {
        if (isNewOperation) {
            currentNumber = "0."
            isNewOperation = false
        } else if (!currentNumber.contains(".")) {
            currentNumber += "."
        }
        updateDisplay()
    }

    private fun onSignClick() {
        if (currentNumber.isNotEmpty() && currentNumber != "0") {
            currentNumber = if (currentNumber.startsWith("-")) {
                currentNumber.substring(1)
            } else {
                "-$currentNumber"
            }
            updateDisplay()
        }
    }

    private fun updateDisplay() {
        dispNum.text = currentNumber.ifEmpty { "0" }
    }
}
