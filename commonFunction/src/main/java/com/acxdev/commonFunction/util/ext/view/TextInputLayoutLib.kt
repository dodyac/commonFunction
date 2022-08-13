package com.acxdev.commonFunction.util.ext.view

import android.content.Context
import android.text.format.DateFormat
import android.widget.ArrayAdapter
import androidx.annotation.ArrayRes
import com.acxdev.commonFunction.R
import com.acxdev.commonFunction.util.ext.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

//TextInputLayout

fun Context.alertAuth(mail: TextInputLayout, password: TextInputLayout): Boolean {
    when {
        mail.toEditString().isEmpty() -> {
            mail.isErrorEnabled = true
            mail.error = getString(R.string.emptyMail)
            mail.requestFocus()
            return false
        }
        !mail.toEditString().isEmailValid() -> {
            mail.isErrorEnabled = true
            mail.error = getString(R.string.notMail)
            mail.clearFocus()
            mail.requestFocus()
            return false
        }
        password.toEditString().isEmpty() -> {
            password.isErrorEnabled = true
            password.error = getString(R.string.emptyPassword)
            password.requestFocus()
            return false
        }
        password.toEditString().length < 8 -> {
            password.isErrorEnabled = true
            password.error = getString(R.string.shortPassword)
            password.clearFocus()
            password.requestFocus()
            return false
        }
        else -> {
            mail.isErrorEnabled = false
            password.isErrorEnabled = false
            return true
        }
    }
}

fun TextInputLayout.alertMail(): Boolean {
    when {
        toEditString().isEmpty() -> {
            isErrorEnabled = true
            error = context.getCompatActivity().getString(R.string.emptyMail)
            requestFocus()
            return false
        }
        !toEditString().isEmailValid() -> {
            isErrorEnabled = true
            error = context.getCompatActivity().getString(R.string.notMail)
            clearFocus()
            requestFocus()
            return false
        }
        else -> {
            isErrorEnabled = false
            return true
        }
    }
}

fun TextInputLayout.alertPassword(): Boolean {
    when {
        toEditString().isEmpty() -> {
            isErrorEnabled = true
            error = context.getCompatActivity().getString(R.string.emptyPassword)
            requestFocus()
            return false
        }
        toEditString().length < 8 -> {
            isErrorEnabled = true
            error = context.getCompatActivity().getString(R.string.shortPassword)
            clearFocus()
            requestFocus()
            return false
        }
        else -> {
            isErrorEnabled = false
            return true
        }
    }
}

fun TextInputLayout.isNotEmpty(): Boolean {
    return if(toEditString().isEmpty()) {
        isErrorEnabled = true
        error = "$hint ${context.getString(R.string.cannotEmpty)}"
        requestFocus()
        false
    } else {
        isErrorEnabled = false
        clearFocus()
        true
    }
}

fun TextInputLayout.toEditString(): String = editText?.text.toString()

fun MaterialAutoCompleteTextView.toEditString(): String = text.toString()

fun TextInputLayout.setText(string: String?) {
    editText?.setText(string)
}

//MaterialAutoCompleteTextView

fun MaterialAutoCompleteTextView.set(@ArrayRes array: Int) {
    val dataAdapter = ArrayAdapter(
        context.getCompatActivity(),
        android.R.layout.simple_spinner_dropdown_item,
        context.resources.getStringArray(array)
    )
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    setAdapter(dataAdapter)
}

fun MaterialAutoCompleteTextView.set(list: List<String>) {
    val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, list)
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    setAdapter(dataAdapter)
}

fun MaterialAutoCompleteTextView.isNotEmpty(textInputLayout: TextInputLayout): Boolean {
    return if(text.isNullOrEmpty()) {
        textInputLayout.isErrorEnabled = true
        textInputLayout.error = "$hint ${context.getString(R.string.cannotEmpty)}"
        textInputLayout.requestFocus()
        false
    } else {
        textInputLayout.isErrorEnabled = false
        textInputLayout.clearFocus()
        true
    }
}