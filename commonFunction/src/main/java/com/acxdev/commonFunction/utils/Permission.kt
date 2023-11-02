package com.acxdev.commonFunction.utils

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

object Permission {

    fun AppCompatActivity.permissionLauncher(
        result: Result.() -> Unit
    ): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val isAllPermissionsGranted = permissions.values.all { it }

            if (isAllPermissionsGranted) {
                result.invoke(Result.Granted)
            } else {
                result.invoke(Result.Denied)
            }
        }
    }

    fun Fragment.permissionLauncher(
        result: Result.() -> Unit
    ): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val isAllPermissionsGranted = permissions.values.all { it }

            if (isAllPermissionsGranted) {
                result.invoke(Result.Granted)
            } else {
                result.invoke(Result.Denied)
            }
        }
    }

    enum class Result {
        Granted,
        Denied
    }
}