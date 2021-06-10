package com.acxdev.commonFunction.common

import android.view.LayoutInflater
import android.view.ViewGroup

typealias InflateAc<T> = (LayoutInflater) -> T

typealias InflateFr<T> = (LayoutInflater, ViewGroup?, Boolean) -> T