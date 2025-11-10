package com.example.w8_b1

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var display : TextView
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner

    private val numberButtons = mutableListOf<Button>()
    private val operatorButtons = mutableMapOf<String, Button>()
    private val functionButtons = mutableMapOf<String, Button>()

    private var firstValue = ""
    private var secondValue = ""
    private var selectedOperator = ""
    private var startNewInput = true
    private var allowConversion = true

    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "VND" to 0.000038,
        "EUR" to 1.149425,
        "JPY" to 0.0065,
        "GBP" to 1.298701,
        "AUD" to 0.64,
        "CAD" to 0.74,
        "CHF" to 1.10,
        "CNY" to 0.14,
        "HKD" to 0.13
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initializeViews()
        setupCurrencySpinners()
        setupButtonListeners()
        display.text = "0"
    }

    private fun initializeViews() {
        display = findViewById(R.id.dispNum)

        val buttonIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )
        buttonIds.forEach { numberButtons.add(findViewById(it)) }

        operatorButtons["+"] = findViewById(R.id.btnAdd)
        operatorButtons["-"] = findViewById(R.id.btnSub)
        operatorButtons["×"] = findViewById(R.id.btnMult)
        operatorButtons["/"] = findViewById(R.id.btnDiv)

        functionButtons["="] = findViewById(R.id.btnEqual)
        functionButtons["C"] = findViewById(R.id.btnC)
        functionButtons["CE"] = findViewById(R.id.btnCE)
        functionButtons["BS"] = findViewById(R.id.btnBS)
        functionButtons["."] = findViewById(R.id.btnDot)
        functionButtons["+/-"] = findViewById(R.id.btnSign)
        functionButtons["Convert"] = findViewById(R.id.btnConvert)
    }

    private fun setupCurrencySpinners() {
        val currencyList = arrayOf("VND", "USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "CNY", "HKD")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)

        spinnerFrom = findViewById(R.id.spinnerFirstCur)
        spinnerTo = findViewById(R.id.spinnerSecondCur)

        listOf(spinnerFrom, spinnerTo).forEach { spinner ->
            spinner.adapter = spinnerAdapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun setupButtonListeners() {
        numberButtons.forEach { btn ->
            btn.setOnClickListener { handleNumberInput(btn.text.toString()) }
        }

        operatorButtons.forEach { (op, btn) ->
            btn.setOnClickListener { handleOperator(op) }
        }

        functionButtons["="]?.setOnClickListener { executeCalculation() }
        functionButtons["C"]?.setOnClickListener { resetCalculator() }
        functionButtons["CE"]?.setOnClickListener { clearCurrentEntry() }
        functionButtons["BS"]?.setOnClickListener { removeLastDigit() }
        functionButtons["."]?.setOnClickListener { addDecimalPoint() }
        functionButtons["+/-"]?.setOnClickListener { toggleSign() }
        functionButtons["Convert"]?.setOnClickListener {
            if (allowConversion) {
                convertCurrency(spinnerFrom.selectedItem.toString(), spinnerTo.selectedItem.toString())
            }
        }
    }

    private fun handleNumberInput(digit: String) {
        secondValue = if (startNewInput) digit else secondValue + digit
        startNewInput = false
        allowConversion = true
        refreshDisplay()
    }

    private fun handleOperator(op: String) {
        when {
            secondValue.isNotEmpty() -> {
                if (firstValue.isNotEmpty() && selectedOperator.isNotEmpty() && !startNewInput) {
                    compute()
                }
                firstValue = secondValue
                selectedOperator = op
                startNewInput = true
            }
            firstValue.isNotEmpty() -> selectedOperator = op
        }
    }

    private fun executeCalculation() {
        if (firstValue.isNotEmpty() && secondValue.isNotEmpty() && selectedOperator.isNotEmpty()) {
            compute()
            selectedOperator = ""
            firstValue = ""
        }
    }

    private fun compute() {
        try {
            val value1 = firstValue.toDouble()
            val value2 = secondValue.toDouble()

            val outcome = when (selectedOperator) {
                "+" -> value1 + value2
                "-" -> value1 - value2
                "×" -> value1 * value2
                "/" -> {
                    if (value2 == 0.0) {
                        showError()
                        return
                    }
                    value1 / value2
                }
                else -> return
            }

            secondValue = if (outcome % 1.0 == 0.0) outcome.toLong().toString() else outcome.toString()
            startNewInput = true
            refreshDisplay()
        } catch (_: Exception) {
            showError()
        }
    }

    private fun convertCurrency(sourceCurrency: String, targetCurrency: String) {
        val inputValue = if (secondValue.isNotEmpty() && !startNewInput) secondValue else display.text.toString()

        val parsedAmount = try {
            inputValue.replace(" ", "").toDouble()
        } catch (_: Exception) {
            display.text = "Error"
            return
        }

        val sourceRate = exchangeRates[sourceCurrency] ?: run {
            display.text = "Unsupported"
            return
        }

        val targetRate = exchangeRates[targetCurrency] ?: run {
            display.text = "Unsupported"
            return
        }

        val usdValue = parsedAmount * sourceRate
        val finalAmount = if (targetRate != 0.0) usdValue / targetRate else {
            display.text = "Error"
            return
        }

        secondValue = if (finalAmount % 1.0 == 0.0) {
            finalAmount.toDouble().toString()
        } else {
            String.format(java.util.Locale.US, "%.4f", finalAmount).trimEnd('0').trimEnd('.')
        }

        startNewInput = true
        allowConversion = false
        refreshDisplay()
    }

    private fun resetCalculator() {
        secondValue = ""
        firstValue = ""
        selectedOperator = ""
        startNewInput = true
        display.text = "0"
    }

    private fun clearCurrentEntry() {
        secondValue = ""
        startNewInput = true
        display.text = "0"
    }

    private fun removeLastDigit() {
        if (secondValue.isNotEmpty() && !startNewInput) {
            secondValue = secondValue.dropLast(1)
            refreshDisplay()
        }
    }

    private fun addDecimalPoint() {
        if (startNewInput) {
            secondValue = "0."
            startNewInput = false
        } else if (!secondValue.contains(".")) {
            secondValue += "."
        }
        refreshDisplay()
    }

    private fun toggleSign() {
        if (secondValue.isNotEmpty() && secondValue != "0") {
            secondValue = if (secondValue.startsWith("-")) {
                secondValue.substring(1)
            } else {
                "-$secondValue"
            }
            refreshDisplay()
        }
    }

    private fun showError() {
        display.text = "Error"
        secondValue = ""
        firstValue = ""
        selectedOperator = ""
        startNewInput = true
    }

    private fun refreshDisplay() {
        display.text = if (secondValue.isEmpty()) "0" else secondValue
    }
}
