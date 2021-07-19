package com.acxdev.commonFunction.util.view

import android.content.Context
import android.text.format.DateFormat
import android.widget.ArrayAdapter
import androidx.annotation.StringRes
import com.acxdev.commonFunction.R
import com.acxdev.commonFunction.common.Language
import com.acxdev.commonFunction.util.FunctionX.Companion.getCompatActivity
import com.acxdev.commonFunction.util.DataTypeX.Companion.add0
import com.acxdev.commonFunction.util.DataTypeX.Companion.isEmailValid
import com.acxdev.commonFunction.util.DataTypeX.Companion.toDate
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class TextInputLayoutX {
    companion object{

        //TextInputLayout

        fun TextInputLayout.datePicker(title: String, format: String){
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText(title)
            val datePicker = builder.build()
            datePicker.show((context.getCompatActivity())!!.supportFragmentManager, "DATE_PICKER")
            datePicker.addOnPositiveButtonClickListener { editText!!.setText(it.toDate(format)) }
        }

        fun TextInputLayout.timePicker(title: String, timeFormat: Int? = null){
            val isSystem24Hour = DateFormat.is24HourFormat(context.getCompatActivity()!!)
            val clockFormat = if(isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(timeFormat ?: clockFormat)
                .setHour(12)
                .setMinute(0)
                .setTitleText(title)
                .build()
            picker.show((context.getCompatActivity())!!.supportFragmentManager, "TIME_PICKER")
            picker.addOnPositiveButtonClickListener {editText!!.setText("${picker.hour.toString().add0()}:${picker.minute.toString().add0()}") }
        }

        fun TextInputLayout.datePickerWithTime(title: String, titleTimePicker: String, format: String, timeFormat: Int? = null){
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText(title)
            val datePicker = builder.build()
            datePicker.show((context.getCompatActivity())!!.supportFragmentManager, "DATE_PICKER")
            datePicker.addOnPositiveButtonClickListener {
                val isSystem24Hour = DateFormat.is24HourFormat(context.getCompatActivity()!!)
                val clockFormat = if(isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
                val picker = MaterialTimePicker.Builder()
                    .setTimeFormat(timeFormat ?: clockFormat)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText(titleTimePicker)
                    .build()
                picker.show((context.getCompatActivity())!!.supportFragmentManager, "TIME_PICKER")
                picker.addOnPositiveButtonClickListener {_-> editText!!.setText("${picker.hour.toString().add0()}:${picker.minute.toString().add0()} - ${it.toDate(format)}") }
            }
        }

        fun Context.alertAuth(mail: TextInputLayout, password: TextInputLayout): Boolean {
            when {
                mail.editText!!.text.isEmpty() -> {
                    mail.isErrorEnabled = true
                    mail.error = getString(R.string.emptyMail)
                    mail.requestFocus()
                    return false
                }
                !mail.editText!!.text.isEmailValid() -> {
                    mail.isErrorEnabled = true
                    mail.error = getString(R.string.notMail)
                    mail.clearFocus()
                    mail.requestFocus()
                    return false
                }
                password.editText!!.text.isEmpty() -> {
                    password.isErrorEnabled = true
                    password.error = getString(R.string.emptyPassword)
                    password.requestFocus()
                    return false
                }
                password.editText!!.text.length < 8 -> {
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
                editText!!.text.isEmpty() -> {
                    isErrorEnabled = true
                    error = context.getCompatActivity()!!.getString(R.string.emptyMail)
                    requestFocus()
                    return false
                }
                !editText!!.text.isEmailValid() -> {
                    isErrorEnabled = true
                    error = context.getCompatActivity()!!.getString(R.string.notMail)
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
                editText!!.text.isEmpty() -> {
                    isErrorEnabled = true
                    error = context.getCompatActivity()!!.getString(R.string.emptyPassword)
                    requestFocus()
                    return false
                }
                editText!!.text.length < 8 -> {
                    isErrorEnabled = true
                    error = context.getCompatActivity()!!.getString(R.string.shortPassword)
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

        fun TextInputLayout.alertEmpty(language: Language): Boolean {
            return if(editText!!.text.isEmpty()) {
                isErrorEnabled = true
                val alert = when(language){
                    Language.EN -> "cannot be empty!"
                    Language.ID -> "tidak boleh kosong!"
                }
                error = "$hint $alert"
                requestFocus()
                false
            } else {
                isErrorEnabled = false
                clearFocus()
                true
            }
        }

        //MaterialAutoCompleteTextView

        fun MaterialAutoCompleteTextView.set(@StringRes array: Int) {
            val dataAdapter = ArrayAdapter(context.getCompatActivity()!!, android.R.layout.simple_spinner_dropdown_item, context.resources.getStringArray(array))
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            setAdapter(dataAdapter)
        }
    }
}