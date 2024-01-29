package com.acxdev.commonFunction.utils.ext.view

object TilValidationHolder {

    enum class Validation {
        NotEmpty,
        GreaterThan,
        GreaterThanEqual,
        Equal,
        LessThan,
        LessThanEqual,
    }

    var notEmpty: String = "tidak boleh kosong"
    var greaterThan: String = "harus lebih dari "
    var greaterThanEqual: String = "harus lebih dari sama dengan "
    var equal: String = "tidak boleh "
    var lessThan: String = "harus kurang dari "
    var lessThanEqual: String = "harus kurang dari sama dengan "

    fun Validation.setErrorMessage(msg: String) {
        when(this) {
            Validation.NotEmpty -> {
                notEmpty = msg
            }
            Validation.GreaterThan ->  {
                greaterThan = msg
            }
            Validation.GreaterThanEqual ->  {
                greaterThanEqual = msg
            }
            Validation.Equal ->  {
                equal = msg
            }
            Validation.LessThan -> {
                lessThan = msg
            }
            Validation.LessThanEqual -> {
                lessThanEqual = msg
            }
        }
    }
}