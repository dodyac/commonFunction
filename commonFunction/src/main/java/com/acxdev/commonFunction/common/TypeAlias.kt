package com.acxdev.commonFunction.common

import android.view.LayoutInflater
import android.view.ViewGroup

typealias Inflate<T> = (LayoutInflater) -> T

typealias InflateViewGroup<T> = (LayoutInflater, ViewGroup?, Boolean) -> T