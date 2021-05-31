package com.acdev.commonFunction.util.view

import android.content.Context
import android.text.format.DateFormat
import android.widget.ArrayAdapter
import androidx.annotation.StringRes
import com.acdev.commonFunction.R
import com.acdev.commonFunction.common.Constantx
import com.acdev.commonFunction.common.Region
import com.acdev.commonFunction.model.BankRegion
import com.acdev.commonFunction.util.Functionx.Companion.getCompatActivity
import com.acdev.commonFunction.util.LibQue.Companion.libque
import com.acdev.commonFunction.util.DataType.Companion.add0
import com.acdev.commonFunction.util.DataType.Companion.isEmailValid
import com.acdev.commonFunction.util.DataType.Companion.toDate
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import retrofit2.Call

class TextInputLayout {
    companion object{

        //TextInputLayout

        fun TextInputLayout.datePicker(title: String, format: String){
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText(title)
            val datePicker = builder.build()
            datePicker.show((this.context.getCompatActivity())!!.supportFragmentManager, "DATE_PICKER")
            datePicker.addOnPositiveButtonClickListener { this.editText!!.setText(it.toDate(format)) }
        }

        fun TextInputLayout.timePicker(title: String, timeFormat: Int? = null){
            val isSystem24Hour = DateFormat.is24HourFormat(this.context.getCompatActivity()!!)
            val clockFormat = if(isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(timeFormat ?: clockFormat)
                .setHour(12)
                .setMinute(0)
                .setTitleText(title)
                .build()
            picker.show((this.context.getCompatActivity())!!.supportFragmentManager, "TIME_PICKER")
            picker.addOnPositiveButtonClickListener {c-> this.editText!!.setText("${picker.hour.toString().add0()}:${picker.minute.toString().add0()}") }
        }

        fun TextInputLayout.datePickerWithTime(title: String, titleTimePicker: String, format: String, timeFormat: Int? = null){
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText(title)
            val datePicker = builder.build()
            datePicker.show((this.context.getCompatActivity())!!.supportFragmentManager, "DATE_PICKER")
            datePicker.addOnPositiveButtonClickListener {
                val isSystem24Hour = DateFormat.is24HourFormat(this.context.getCompatActivity()!!)
                val clockFormat = if(isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
                val picker = MaterialTimePicker.Builder()
                    .setTimeFormat(timeFormat ?: clockFormat)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText(titleTimePicker)
                    .build()
                picker.show((this.context.getCompatActivity())!!.supportFragmentManager, "TIME_PICKER")
                picker.addOnPositiveButtonClickListener {c-> this.editText!!.setText("${picker.hour.toString().add0()}:${picker.minute.toString().add0()} - ${it.toDate(format)}") }
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
                this.editText!!.text.isEmpty() -> {
                    this.isErrorEnabled = true
                    this.error = this.context.getCompatActivity()!!.getString(R.string.emptyMail)
                    this.requestFocus()
                    return false
                }
                !this.editText!!.text.isEmailValid() -> {
                    this.isErrorEnabled = true
                    this.error = this.context.getCompatActivity()!!.getString(R.string.notMail)
                    this.clearFocus()
                    this.requestFocus()
                    return false
                }
                else -> {
                    this.isErrorEnabled = false
                    return true
                }
            }
        }

        fun TextInputLayout.alertPassword(): Boolean {
            when {
                this.editText!!.text.isEmpty() -> {
                    this.isErrorEnabled = true
                    this.error = this.context.getCompatActivity()!!.getString(R.string.emptyPassword)
                    this.requestFocus()
                    return false
                }
                this.editText!!.text.length < 8 -> {
                    this.isErrorEnabled = true
                    this.error = this.context.getCompatActivity()!!.getString(R.string.shortPassword)
                    this.clearFocus()
                    this.requestFocus()
                    return false
                }
                else -> {
                    this.isErrorEnabled = false
                    return true
                }
            }
        }

        fun TextInputLayout.alertEmpty(@StringRes alert: Int): Boolean {
            return if(this.editText!!.text.isEmpty()) {
                this.isErrorEnabled = true
                this.error = this.context.getCompatActivity()!!.getString(alert)
                this.requestFocus()
                false
            } else {
                this.isErrorEnabled = false
                this.clearFocus()
                true
            }
        }

        //MaterialAutoCompleteTextView

        @Suppress("UNCHECKED_CAST")
        fun MaterialAutoCompleteTextView.setStringArray(stringArray: Array<String?>) {
            val lst: List<String> = listOf(*stringArray) as List<String>
            val dataAdapter = ArrayAdapter(this.context.getCompatActivity()!!, android.R.layout.simple_spinner_dropdown_item, lst)
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            this.setAdapter(dataAdapter)
        }

        fun MaterialAutoCompleteTextView.getBank(call: Call<BankRegion?>?) {
            call?.libque(this.context) {
                val modelDataArrayList = this.body()!!.data
                val array = arrayOfNulls<String>(modelDataArrayList.size)
                for ((index, value) in modelDataArrayList.withIndex()) array[index] = value.nama
                this@getBank.setStringArray(array)
            }
        }

        fun MaterialAutoCompleteTextView.getRegion(call: Call<BankRegion?>?, region: Region) {
            call?.libque(this.context) {
                when (region) {
                    Region.PROVINCE -> Constantx.PROVINCE = this.body()!!.data
                    Region.CITY -> Constantx.CITY = this.body()!!.data
                    Region.DISTRICT -> Constantx.DISTRICT = this.body()!!.data
                    Region.VILLAGE -> Constantx.VILLAGE = this.body()!!.data
                }
                val array = arrayOfNulls<String>(this.body()!!.data.size)
                for ((index, value) in this.body()!!.data.withIndex()) array[index] = value.nama
                this@getRegion.setStringArray(array)
            }
        }
    }
}