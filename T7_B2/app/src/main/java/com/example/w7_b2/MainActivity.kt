package com.example.w7_b2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.CalendarView
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var textInputLayoutFName: TextInputLayout
    private lateinit var textInputFName: TextInputEditText
    private lateinit var textInputLNameLayout: TextInputLayout
    private lateinit var textInputLName: TextInputEditText
    private lateinit var textInputBirthdayLayout: TextInputLayout
    private lateinit var textInputBirthday: TextInputEditText
    private lateinit var textInputAddressLayout: TextInputLayout
    private lateinit var textInputAddress: TextInputEditText
    private lateinit var textInputEmailLayout: TextInputLayout
    private lateinit var textInputEmail: TextInputEditText
    private lateinit var btnSelect: Button
    private lateinit var btnRegister: Button
    private lateinit var viewCalendar: CalendarView
    private lateinit var radioBtnMale: RadioButton
    private lateinit var radioBtnFemale: RadioButton
    private lateinit var chkBoxAgree: CheckBox

    companion object {
        private const val COLOR_RED = 0xFFE62E2E.toInt()
        private const val COLOR_GREEN = 0xFF1CE83D.toInt()
        private const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        private const val BIRTHDAY_PATTERN = "([1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/((19|20)\\d\\d)"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        textInputLayoutFName = findViewById(R.id.textInputFirstName)
        textInputFName = findViewById(R.id.editTextFirstName)
        textInputLNameLayout = findViewById(R.id.textInputLastName)
        textInputLName = findViewById(R.id.editTextLastName)
        textInputBirthdayLayout = findViewById(R.id.textInputBirthday)
        textInputBirthday = findViewById(R.id.editTextBirthday)
        textInputAddressLayout = findViewById(R.id.textInputAddress)
        textInputAddress = findViewById(R.id.editTextAddress)
        textInputEmailLayout = findViewById(R.id.textInputEmail)
        textInputEmail = findViewById(R.id.editTextEmail)
        radioBtnMale = findViewById(R.id.checkBoxMale)
        radioBtnFemale = findViewById(R.id.checkBoxFemale)
        chkBoxAgree = findViewById(R.id.checkBoxAgree)
        btnSelect = findViewById(R.id.selectBtn)
        btnRegister = findViewById(R.id.registerBtn)
        viewCalendar = findViewById(R.id.calendarView)
        viewCalendar.visibility = CalendarView.GONE
    }

    private fun setupListeners() {
        btnSelect.setOnClickListener {
            viewCalendar.visibility = if (viewCalendar.visibility == CalendarView.VISIBLE) {
                CalendarView.GONE
            } else {
                CalendarView.VISIBLE
            }
        }

        viewCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            textInputBirthday.setText("$dayOfMonth/${month + 1}/$year")
        }

        btnRegister.setOnClickListener {
            validateAndSubmit()
        }
    }

    private fun validateAndSubmit() {
        val isFirstNameValid = validateField(textInputFName)
        val isLastNameValid = validateField(textInputLName)
        val isEmailValid = validateEmail()
        val isAddressValid = validateField(textInputAddress)
        val isBirthdayValid = validateBirthday()
        val isGenderValid = validateGender()
        val isAgreementValid = validateAgreement()

        if (isFirstNameValid && isLastNameValid && isEmailValid && isAddressValid && isBirthdayValid) {
            setFieldColor(textInputFName, COLOR_GREEN)
            setFieldColor(textInputLName, COLOR_GREEN)
            setFieldColor(textInputEmail, COLOR_GREEN)
            setFieldColor(textInputAddress, COLOR_GREEN)
            setFieldColor(textInputBirthday, COLOR_GREEN)
        }
    }

    private fun validateField(field: TextInputEditText): Boolean {
        return if (field.text.isNullOrBlank()) {
            setFieldColor(field, COLOR_RED)
            false
        } else {
            true
        }
    }

    private fun validateEmail(): Boolean {
        return when {
            textInputEmail.text.isNullOrBlank() -> {
                setFieldColor(textInputEmail, COLOR_RED)
                false
            }
            !textInputEmail.text.toString().trim().matches(EMAIL_PATTERN.toRegex()) -> {
                textInputEmailLayout.error = "Invalid email format"
                false
            }
            else -> {
                textInputEmailLayout.error = null
                true
            }
        }
    }

    private fun validateBirthday(): Boolean {
        return when {
            textInputBirthday.text.isNullOrBlank() -> {
                setFieldColor(textInputBirthday, COLOR_RED)
                false
            }
            !textInputBirthday.text.toString().trim().matches(BIRTHDAY_PATTERN.toRegex()) -> {
                textInputBirthdayLayout.error = "Invalid birthday format dd/mm/yyyy"
                false
            }
            else -> {
                textInputBirthdayLayout.error = null
                true
            }
        }
    }

    private fun validateGender(): Boolean {
        return if (!radioBtnMale.isChecked && !radioBtnFemale.isChecked) {
            radioBtnMale.error = "Please select gender option"
            radioBtnFemale.error = "Please select gender option"
            false
        } else {
            radioBtnMale.error = null
            radioBtnFemale.error = null
            true
        }
    }

    private fun validateAgreement(): Boolean {
        return if (!chkBoxAgree.isChecked) {
            chkBoxAgree.error = "You must agree to the terms and conditions"
            false
        } else {
            chkBoxAgree.error = null
            true
        }
    }

    private fun setFieldColor(field: TextInputEditText, color: Int) {
        field.setBackgroundColor(color)
    }
}
