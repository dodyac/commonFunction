package com.acxdev.commonFunction.util.ext.view

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.util.Base64.*
import android.util.Log
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.acxdev.commonFunction.R
import com.acxdev.commonFunction.util.TileDrawable
import com.acxdev.commonFunction.util.ext.emptyString
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.io.ByteArrayOutputStream

fun ImageView.setImage64(base64: String?) {
    Glide.with(context).load(decode(base64, DEFAULT))
        .transform(RoundedCorners(8)).into(this)
}

fun ImageView.setImageUrl(
    url: String,
    @ColorInt color: Int = Color.GRAY,
    errorDrawable: Int = R.drawable.ic_transparent
) {
    val centerRadiuss = (if(layoutParams.width < layoutParams.height) {
        layoutParams.width
    } else {
        layoutParams.height
    }).toFloat() / 4
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.apply {
        strokeWidth = 2f
        centerRadius = centerRadiuss
        start()
        setColorSchemeColors(color)
    }
    Glide.with(context).load(url)
        .placeholder(circularProgressDrawable)
        .error(AppCompatResources.getDrawable(context, errorDrawable))
        .into(this)
}

fun ImageView.default(name: String?, @ColorInt color: Int) {
    val textDrawable = TextDrawable.builder()
        .beginConfig()
        .width(120)
        .height(120)
        .bold()
        .toUpperCase()
        .textColor(color)
        .endConfig()
        .buildRound(name?.substring(0, 1), Color.WHITE)
    this.setImageDrawable(textDrawable)
}

fun ImageView.defaultMaterial(name: String?) {
    val textDrawable = TextDrawable.builder()
        .beginConfig()
        .width(120)
        .height(120)
        .bold()
        .toUpperCase()
        .textColor(Color.WHITE)
        .useFont(Typeface.SANS_SERIF)
        .endConfig()
        .buildRect(name?.substring(0, 1), ColorGenerator.MATERIAL.randomColor)
    this.setImageDrawable(textDrawable)
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