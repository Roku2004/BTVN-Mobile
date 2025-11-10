package com.example.w8_b2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var textInput: EditText
    private lateinit var radioBtnOdd: RadioButton
    private lateinit var radioBtnEven: RadioButton
    private lateinit var radioBtnPrime: RadioButton
    private lateinit var radioBtnPfNum: RadioButton
    private lateinit var radioBtnFibo: RadioButton
    private lateinit var radioBtnSqNum: RadioButton
    private lateinit var radioGroup: RadioGroup
    private lateinit var listView: ListView
    private lateinit var textViewMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupWindowInsets()
        initializeViews()
        setupListeners()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initializeViews() {
        textInput = findViewById(R.id.editText)
        radioBtnOdd = findViewById(R.id.radioBtnOdd)
        radioBtnEven = findViewById(R.id.radioBtnEven)
        radioBtnPrime = findViewById(R.id.radioBtnPrime)
        radioBtnPfNum = findViewById(R.id.radioBtnPfNum)
        radioBtnFibo = findViewById(R.id.radioBtnFibonacci)
        radioBtnSqNum = findViewById(R.id.radioBtnSqNum)
        radioGroup = findViewById(R.id.radioGroup)
        listView = findViewById(R.id.listView)
        textViewMessage = findViewById(R.id.textViewMessage)
    }

    private fun setupListeners() {
        radioGroup.setOnCheckedChangeListener { _, _ -> updateListView() }

        textInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateListView()
            }
        })
    }

    private fun updateListView() {
        try {
            val n = textInput.text.toString().toIntOrNull()

            if (n == null || n <= 0) {
                hideAllViews()
                return
            }

            val listNumber = when {
                radioBtnOdd.isChecked -> generateOddNumbers(n)
                radioBtnEven.isChecked -> generateEvenNumbers(n)
                radioBtnFibo.isChecked -> generateFibonacciNumbers(n)
                radioBtnPfNum.isChecked -> generatePerfectNumbers(n)
                radioBtnPrime.isChecked -> generatePrimeNumbers(n)
                radioBtnSqNum.isChecked -> generateSquareNumbers(n)
                else -> {
                    hideAllViews()
                    return
                }
            }

            displayResults(listNumber)
        } catch (e: Exception) {
            hideAllViews()
        }
    }

    private fun displayResults(numbers: List<String>) {
        if (numbers.isEmpty()) {
            listView.visibility = View.GONE
            textViewMessage.visibility = View.VISIBLE
        } else {
            textViewMessage.visibility = View.GONE
            listView.visibility = View.VISIBLE
            listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, numbers)
        }
    }

    private fun hideAllViews() {
        listView.visibility = View.GONE
        textViewMessage.visibility = View.GONE
    }

    private fun generatePrimeNumbers(n: Int): List<String> {
        return (2..n).filter { num ->
            (2 until num).none { num % it == 0 }
        }.map { it.toString() }
    }

    private fun generatePerfectNumbers(n: Int): List<String> {
        return (2..n).filter { num ->
            (1 until num).filter { num % it == 0 }.sum() == num
        }.map { it.toString() }
    }

    private fun generateFibonacciNumbers(n: Int): List<String> {
        val result = mutableListOf<String>()
        var a = 0
        var b = 1
        while (a <= n) {
            result.add(a.toString())
            val next = a + b
            a = b
            b = next
        }
        return result
    }

    private fun generateSquareNumbers(n: Int): List<String> {
        return generateSequence(1) { it + 1 }
            .map { it * it }
            .takeWhile { it <= n }
            .map { it.toString() }
            .toList()
    }

    private fun generateOddNumbers(n: Int): List<String> {
        return (1..n step 2).map { it.toString() }
    }

    private fun generateEvenNumbers(n: Int): List<String> {
        return (2..n step 2).map { it.toString() }
    }
}
