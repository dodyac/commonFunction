package com.acxdev.commonFunction.utils.ext.view

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.util.Base64.*
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.acxdev.commonFunction.utils.TileDrawable
import com.acxdev.commonFunction.utils.ext.emptyString
import java.io.ByteArrayOutputStream

fun ImageView.toBase64(): String {
    return try {
        val byteArrayOutputStream = ByteArrayOutputStream()
        (drawable as BitmapDrawable).bitmap.compress(
            Bitmap.CompressFormat.PNG,
            100,
            byteArrayOutputStream
        )
        val byteArray = byteArrayOutputStream.toByteArray()
        encodeToString(byteArray, DEFAULT)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyString()
    }
}

fun ImageView.tint(@ColorInt color: Int) {
    ImageViewCompat.setImageTintList(
        this,
        ColorStateList.valueOf(color)
    )
}

fun ImageView.setPattern(@DrawableRes drawableRes: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, drawableRes)
        ?.let { TileDrawable(it, Shader.TileMode.REPEAT) })
}