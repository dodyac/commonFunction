package com.acxdev.commonFunction.utils.ext.view

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.util.Base64.*
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.acxdev.commonFunction.R
import com.acxdev.commonFunction.utils.TileDrawable
import com.acxdev.commonFunction.utils.ext.emptyString
import com.amulyakhare.textdrawable.TextDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.io.ByteArrayOutputStream

fun ImageView.setImage64(base64: String?) {
    Glide.with(context).load(decode(base64, DEFAULT))
        .transform(RoundedCorners(8)).into(this)
}

fun ImageView.setImageUrl(
    url: String,
    errorDrawable: Int = R.drawable.ic_transparent
) {
    Glide.with(context).load(url)
//        .error(AppCompatResources.getDrawable(context, errorDrawable))
        .into(this)
}

fun ImageView.default(name: String, @ColorInt textColor: Int, @ColorInt backgroundColor: Int, font: Typeface = Typeface.SANS_SERIF) {
    if(name.isNotEmpty()) {
        val textDrawable = TextDrawable.builder()
            .beginConfig()
            .width(120)
            .height(120)
            .bold()
            .toUpperCase()
            .textColor(textColor)
            .useFont(font)
            .endConfig()
            .buildRound(name.substring(0, 1), backgroundColor)
        setImageDrawable(textDrawable)
    }
}

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