package com.acxdev.commonFunction.utils.ext.view

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.ArrayRes
import androidx.core.util.Pair
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentManager
import com.acxdev.commonFunction.utils.ext.*
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

//TextInputLayout

private const val DATE_PICKER = "DATE_PICKER"
private const val TIME_PICKER = "TIME_PICKER"

enum class IconGravity {
    Start, End
}

data class CalendarDialog(
    val title: String,
    val format: String,
    val locale: Locale = Locale.getDefault(),
    val zoneId: ZoneId = ZoneId.systemDefault(),
    val minDate: LocalDate? = null,
    val maxDate: LocalDate? = null
) {
//    It turns out that the MaterialDatePicker uses UTC time format, which means it doesn't contain
//    to show
    private fun LocalDate.toEpochMilli(): Long {
        return atStartOfDay()
            .atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
            .toInstant()
            .toEpochMilli()
    }

//    to set
    fun toUTCLocalDateTime(timeMillis: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timeMillis),
            ZoneId.ofOffset("UTC", ZoneOffset.UTC)
        )
        return localDateTime
            .atZone(zoneId)
            .toInstant()
            .toEpochMilli()
            .toDate(format, locale)
    }

    fun getSelectedDateTimeMillis(selectedDate: String): Long {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(format, locale)
        val date = LocalDate.parse(selectedDate, formatter)
        return date.toEpochMilli()
    }

    fun getCalendarConstraintValidators(): CalendarConstraints {
        val validators = mutableListOf<CalendarConstraints.DateValidator>()

        minDate?.let {
            val minDate = it.toEpochMilli()
            validators.add(DateValidatorPointForward.from(minDate))
        }

        maxDate?.let {
            val maxDate = it.toEpochMilli()
            validators.add(DateValidatorPointBackward.before(maxDate))
        }

        return CalendarConstraints.Builder()
            .setValidator(CompositeDateValidator.allOf(validators))
            .build()
    }
}

fun FragmentManager.setDatePicker(
    vararg textInputLayouts: TextInputLayout,
    calendarDialog: CalendarDialog,
    iconGravity: IconGravity = IconGravity.Start
) {
    textInputLayouts.forEach { textInputLayout ->
        when(iconGravity) {
            IconGravity.Start -> {
                textInputLayout.setStartIconOnClickListener {
                    showCalendarDialog(
                        textInputLayout = textInputLayout,
                        calendarDialog = calendarDialog
                    )
                }
            }
            IconGravity.End -> {
                textInputLayout.setEndIconOnClickListener {
                    showCalendarDialog(
                        textInputLayout = textInputLayout,
                        calendarDialog = calendarDialog
                    )
                }
            }
        }
    }
}

private fun FragmentManager.showCalendarDialog(
    textInputLayout: TextInputLayout,
    calendarDialog: CalendarDialog
) {

    val builder = MaterialDatePicker.Builder.datePicker()
    builder.setTitleText(calendarDialog.title)
    builder.setCalendarConstraints(calendarDialog.getCalendarConstraintValidators())
    if (textInputLayout.string.isNotEmpty()) {
        val selectedDateTimeMillis = calendarDialog
            .getSelectedDateTimeMillis(textInputLayout.string)
        builder.setSelection(selectedDateTimeMillis)
    }

    val datePicker = builder.build()
    datePicker.show(this, DATE_PICKER)
    datePicker.addOnPositiveButtonClickListener {
        val resultDate = calendarDialog.toUTCLocalDateTime(it)
        textInputLayout.setText(resultDate)
    }
}

fun FragmentManager.showCalendarDialog(
    dateStart: Pair<TextInputLayout, String>,
    dateEnd: Pair<TextInputLayout, String>,
    calendarDialog: CalendarDialog
) {
    val localDateStartTimeMillis = calendarDialog.getSelectedDateTimeMillis(dateStart.second)
    val localDateEndTimeMillis = calendarDialog.getSelectedDateTimeMillis(dateEnd.second)

    val builder = MaterialDatePicker.Builder.dateRangePicker()
    builder.setTitleText(calendarDialog.title)
    builder.setCalendarConstraints(calendarDialog.getCalendarConstraintValidators())
    builder.setSelection(Pair(localDateStartTimeMillis, localDateEndTimeMillis))

    val datePicker = builder.build()
    datePicker.show(this, DATE_PICKER)
    datePicker.addOnPositiveButtonClickListener {
        val resultDateStart = calendarDialog.toUTCLocalDateTime(it.first)
        dateStart.first.setText(resultDateStart)

        val resultDateEnd = calendarDialog.toUTCLocalDateTime(it.second)
        dateEnd.first.setText(resultDateEnd)
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

val TextInputLayout.string: String
    get() = editText?.text.toString()

val TextInputLayout.materialAutoComplete: MaterialAutoCompleteTextView?
    get() = editText as? MaterialAutoCompleteTextView

val TextInputLayout.autoComplete: AutoCompleteTextView?
    get() = editText as? AutoCompleteTextView

sealed class TilValidation {
    data object NotEmpty: TilValidation()
    data class Numeric(
        val numericValidation: NumericValidation,
        val value: Double
    ) : TilValidation()

    fun applyTo(vararg textInputLayouts: TextInputLayout) {
        textInputLayouts.forEach {
            it.addValidations(this)
        }
    }
}

enum class NumericValidation(val errorMsg: String) {
    GreaterThan("harus lebih dari "),
    GreaterThanEqual("harus lebih dari sama dengan "),
    Equal("tidak boleh "),
    LessThan("harus kurang dari "),
    LessThanEqual("harus kurang dari sama dengan "),
}

fun List<TilValidation>.applyTo(vararg textInputLayouts: TextInputLayout) {
    textInputLayouts.forEach {
        it.addValidations(*toTypedArray())
    }
}

fun TextInputLayout.addValidations(vararg tilValidations: TilValidation) {
    fun TextInputLayout.showError(msg: String) {
        isErrorEnabled = true
        error = if (hint.isNullOrEmpty()) {
            msg
        } else {
            "$hint $msg"
        }
    }
    fun TextInputLayout.showErrorNumeric(validation: TilValidation.Numeric) {
        showError("${validation.numericValidation.errorMsg} ${validation.value.toReadable()}")
    }
    fun TextInputLayout.hideError() {
        isErrorEnabled = false
    }

    editText?.doOnTextChanged { text, _, _, _ ->
        for (tilValidation in tilValidations) {
            when(tilValidation) {
                TilValidation.NotEmpty -> {
                    if (text.isNullOrEmpty()) {
                        showError("tidak boleh kosong")
                        break
                    } else {
                        hideError()
                    }
                }
                is TilValidation.Numeric -> {
                    val currentNum = text.toString().toDoubleOrNull() ?: 0.0
                    when(tilValidation.numericValidation) {
                        NumericValidation.GreaterThan -> {
                            if (currentNum > tilValidation.value) {
                                hideError()
                            } else {
                                showErrorNumeric(tilValidation)
                                break
                            }
                        }
                        NumericValidation.GreaterThanEqual -> {
                            if (currentNum >= tilValidation.value) {
                                hideError()
                            } else {
                                showErrorNumeric(tilValidation)
                                break
                            }
                        }
                        NumericValidation.Equal -> {
                            if (currentNum != tilValidation.value) {
                                hideError()
                            } else {
                                showErrorNumeric(tilValidation)
                                break
                            }
                        }
                        NumericValidation.LessThan -> {
                            if (currentNum < tilValidation.value) {
                                hideError()
                            } else {
                                showErrorNumeric(tilValidation)
                                break
                            }
                        }
                        NumericValidation.LessThanEqual -> {
                            if (currentNum > tilValidation.value) {
                                hideError()
                            } else {
                                showErrorNumeric(tilValidation)
                                break
                            }
                        }
                    }
                }
            }
        }
    }
}

fun isNotValid(vararg textInputLayouts: TextInputLayout): Boolean {
    textInputLayouts.forEach {
        it.setText(it.string)
        it.clearFocus()
    }

    return textInputLayouts.any { it.isErrorEnabled }
}

fun TextInputLayout.isNotValid(): Boolean {
    setText(string)
    clearFocus()

    return isErrorEnabled
}