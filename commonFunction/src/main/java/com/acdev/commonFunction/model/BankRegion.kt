package com.acdev.commonFunction.model

data class BankRegion(val code: Long, val success: Boolean, val data: List<Datum>)

data class Datum(val id: Long, val nama: String)