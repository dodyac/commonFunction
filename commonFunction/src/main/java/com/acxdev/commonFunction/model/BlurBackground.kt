package com.acxdev.commonFunction.model

sealed class BlurBackground {
    object None: BlurBackground()
    data class Blur(val radius: Int) : BlurBackground()
}