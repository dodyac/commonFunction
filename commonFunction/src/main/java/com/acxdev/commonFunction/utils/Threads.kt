package com.acxdev.commonFunction.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun AppCompatActivity.doOnBackground(block: suspend () -> Unit) {
    lifecycleScope.launch (Dispatchers.IO) {
        block()
    }
}

fun AppCompatActivity.doOnMain(block: suspend () -> Unit) {
    lifecycleScope.launch (Dispatchers.Main) {
        block()
    }
}
fun Fragment.doOnBackground(block: suspend () -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch (Dispatchers.IO) {
        block()
    }
}

fun Fragment.doOnMain(block: suspend () -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch (Dispatchers.Main) {
        block()
    }
}

fun doOnBackground(block: suspend () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        block()
    }
}

fun doOnMain(block: suspend () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        block()
    }
}