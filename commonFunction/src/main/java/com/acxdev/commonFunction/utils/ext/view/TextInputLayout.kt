package com.acxdev.commonFunction.utils.ext.view

import android.widget.ArrayAdapter
import androidx.annotation.ArrayRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.acxdev.commonFunction.R
import com.acxdev.commonFunction.utils.ext.*
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Locale

//TextInputLayout

private const val DATE_PICKER = "DATE_PICKER"
private const val TIME_PICKER = "TIME_PICKER"

fun AppCompatActivity.setDatePicker(
    vararg textInputLayouts: TextInputLayout,
    title: String,
    format: String,
    locale: Locale = Locale.getDefault(),
    selectedDate: String = getToday(format, locale),
    minDate: Long? = null,
    maxDate: Long? = null,
) {
    val dateString = SimpleDateFormat(format, locale)
    val selectedDateTimeMillis = dateString.parse(selectedDate)?.time

    val validators = mutableListOf<CalendarConstraints.DateValidator>()

    minDate?.let {
        DateValidatorPointForward.from(minDate)
    }

    maxDate?.let {
        DateValidatorPointBackward.before(maxDate)
    }

    val calendarConstraintBuilder = CalendarConstraints.Builder()
        .setValidator(CompositeDateValidator.allOf(validators)).build()

    textInputLayouts.forEach { textInputLayout ->
        textInputLayout.setStartIconOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText(title)
            builder.setCalendarConstraints(calendarConstraintBuilder)
            builder.setSelection(selectedDateTimeMillis)

            val datePicker = builder.build()
            datePicker.show(supportFragmentManager, DATE_PICKER)
            datePicker.addOnPositiveButtonClickListener {
                val resultDate = it.toDate(format, locale)
                textInputLayout.setText(resultDate)
            }
        }
    }
}

fun Fragment.setDatePicker(
    vararg textInputLayouts: TextInputLayout,
    title: String,
    format: String,
    locale: Locale = Locale.getDefault(),
    selectedDate: String = getToday(format, locale),
    minDate: Long? = null,
    maxDate: Long? = null,
) {
    val dateString = SimpleDateFormat(format, locale)
    val selectedDateTimeMillis = dateString.parse(selectedDate)?.time

    val validators = mutableListOf<CalendarConstraints.DateValidator>()

    minDate?.let {
        DateValidatorPointForward.from(minDate)
    }

    maxDate?.let {
        DateValidatorPointBackward.before(maxDate)
    }

    val calendarConstraintBuilder = CalendarConstraints.Builder()
        .setValidator(CompositeDateValidator.allOf(validators)).build()

    textInputLayouts.forEach { textInputLayout ->
        textInputLayout.setStartIconOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText(title)
            builder.setCalendarConstraints(calendarConstraintBuilder)
            builder.setSelection(selectedDateTimeMillis)

            val datePicker = builder.build()
            datePicker.show(childFragmentManager, DATE_PICKER)
            datePicker.addOnPositiveButtonClickListener {
                val resultDate = it.toDate(format, locale)
                textInputLayout.setText(resultDate)
            }
        }
    }
}

//@SuppressLint("SetTextI18n")
//fun TextInputLayout.timePicker(title: String, timeFormat: Int? = null) {
//    setStartIconOnClickListener {
//        val isSystem24Hour = DateFormat.is24HourFormat(context.getActivity())
//        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
//
//        val picker = MaterialTimePicker.Builder()
//            .setTimeFormat(timeFormat ?: clockFormat)
//            .setHour(12)
//            .setMinute(0)
//            .setTitleText(title)
//            .build()
//        picker.show((context.getActivity()).supportFragmentManager, TIME_PICKER)
//        picker.addOnPositiveButtonClickListener {
//            editText?.setText("${picker.hour.toString().add0()}:${picker.minute.toString().add0()}")
//        }
//    }
//}
//
//@SuppressLint("SetTextI18n")
//fun TextInputLayout.datePickerWithTime(
//    title: String,
//    titleTimePicker: String,
//    format: String,
//    timeFormat: Int? = null
//) {
//    setStartIconOnClickListener {
//        val builder = MaterialDatePicker.Builder.datePicker()
//        builder.setTitleText(title)
//        val datePicker = builder.build()
//        datePicker.show((context.getActivity()).supportFragmentManager, DATE_PICKER)
//        datePicker.addOnPositiveButtonClickListener {
//            val isSystem24Hour = DateFormat.is24HourFormat(context.getActivity())
//            val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
//            val picker = MaterialTimePicker.Builder()
//                .setTimeFormat(timeFormat ?: clockFormat)
//                .setHour(12)
//                .setMinute(0)
//                .setTitleText(titleTimePicker)
//                .build()
//            picker.show((context.getActivity()).supportFragmentManager, TIME_PICKER)
//            picker.addOnPositiveButtonClickListener { _ ->
//                editText?.setText("${picker.hour.toString().add0()}:${picker.minute.toString().add0()} - ${it.toDate(format)}")
//            }
//        }
//    }
//}

fun alertAuth(mail: TextInputLayout, password: TextInputLayout, passwordLength: Int = 8): Boolean {
    val context = mail.context
    when {
        mail.toEditString().isEmpty() -> {
            mail.isErrorEnabled = true
            mail.error = context.getString(R.string.emptyMail)
            mail.requestFocus()
            return false
        }
        !mail.toEditString().isEmailValid() -> {
            mail.isErrorEnabled = true
            mail.error = context.getString(R.string.notMail)
            mail.clearFocus()
            mail.requestFocus()
            return false
        }
        password.toEditString().isEmpty() -> {
            password.isErrorEnabled = true
            password.error = context.getString(R.string.emptyPassword)
            password.requestFocus()
            return false
        }
        password.toEditString().length < passwordLength -> {
            password.isErrorEnabled = true
            password.error = context.getString(R.string.shortPassword)
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
            error = context.getString(R.string.emptyMail)
            requestFocus()
            return false
        }
        !toEditString().isEmailValid() -> {
            isErrorEnabled = true
            error = context.getString(R.string.notMail)
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

fun TextInputLayout.alertPassword(passwordLength: Int = 8): Boolean {
    when {
        toEditString().isEmpty() -> {
            isErrorEnabled = true
            error = context.getString(R.string.emptyPassword)
            requestFocus()
            return false
        }
        toEditString().length < passwordLength -> {
            isErrorEnabled = true
            error = context.getString(R.string.shortPassword)
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

fun TextInputLayout.isEmpty(): Boolean {
    return if(toEditString().isEmpty()) {
        isErrorEnabled = true
        error = "$hint ${context.getString(R.string.cannotEmpty)}"
        requestFocus()
        true
    } else {
        isErrorEnabled = false
        clearFocus()
        false
    }
}

fun TextInputLayout.toEditString(): String = editText?.text.toString()

fun TextInputLayout.setText(string: String?) {
    editText?.setText(string)
}

//MaterialAutoCompleteTextView

fun MaterialAutoCompleteTextView.set(@ArrayRes array: Int) {
    val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, context.resources.getStringArray(array))
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    setAdapter(dataAdapter)
}

fun MaterialAutoCompleteTextView.set(list: List<String>) {
    val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, list)
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    setAdapter(dataAdapter)
}

val TextInputLayout.autoComplete: MaterialAutoCompleteTextView?
    get() = editText as? MaterialAutoCompleteTextView