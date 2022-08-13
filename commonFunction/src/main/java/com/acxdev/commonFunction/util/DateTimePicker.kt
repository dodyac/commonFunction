package com.acxdev.commonFunction.util

import android.annotation.SuppressLint
import android.text.format.DateFormat
import com.acxdev.commonFunction.util.ext.add0
import com.acxdev.commonFunction.util.ext.getCompatActivity
import com.acxdev.commonFunction.util.ext.toDate
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class DateTimePicker {
    companion object {
        private const val DATE_PICKER = "DATE_PICKER"
        private const val TIME_PICKER = "TIME_PICKER"

        fun TextInputLayout.datePicker(title: String, format: String) {
            setStartIconOnClickListener {
                val builder = MaterialDatePicker.Builder.datePicker()
                builder.setTitleText(title)
                val datePicker = builder.build()
                datePicker.show((context.getCompatActivity()).supportFragmentManager, DATE_PICKER)
                datePicker.addOnPositiveButtonClickListener { editText?.setText(it.toDate(format)) }
            }
        }

        @SuppressLint("SetTextI18n")
        fun TextInputLayout.timePicker(title: String, timeFormat: Int? = null) {
            setStartIconOnClickListener {
                val isSystem24Hour = DateFormat.is24HourFormat(context.getCompatActivity())
                val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

                val picker = MaterialTimePicker.Builder()
                    .setTimeFormat(timeFormat ?: clockFormat)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText(title)
                    .build()
                picker.show((context.getCompatActivity()).supportFragmentManager, TIME_PICKER)
                picker.addOnPositiveButtonClickListener {
                    editText?.setText("${picker.hour.toString().add0()}:${picker.minute.toString().add0()}")
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun TextInputLayout.datePickerWithTime(
            title: String,
            titleTimePicker: String,
            format: String,
            timeFormat: Int? = null
        ) {
            setStartIconOnClickListener {
                val builder = MaterialDatePicker.Builder.datePicker()
                builder.setTitleText(title)
                val datePicker = builder.build()
                datePicker.show((context.getCompatActivity()).supportFragmentManager, DATE_PICKER)
                datePicker.addOnPositiveButtonClickListener {
                    val isSystem24Hour = DateFormat.is24HourFormat(context.getCompatActivity())
                    val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
                    val picker = MaterialTimePicker.Builder()
                        .setTimeFormat(timeFormat ?: clockFormat)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText(titleTimePicker)
                        .build()
                    picker.show((context.getCompatActivity()).supportFragmentManager, TIME_PICKER)
                    picker.addOnPositiveButtonClickListener { _ ->
                        editText?.setText("${picker.hour.toString().add0()}:${picker.minute.toString().add0()} - ${it.toDate(format)}")
                    }
                }
            }
        }
    }
}