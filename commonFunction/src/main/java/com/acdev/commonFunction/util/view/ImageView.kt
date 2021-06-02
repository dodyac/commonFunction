package com.acdev.commonFunction.util.view

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.io.ByteArrayOutputStream
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.util.Base64.*
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.acdev.commonFunction.util.Functionx.Companion.getCompatActivity
import com.acdev.commonFunction.util.TileDrawable

class ImageView {
    companion object{

        @SuppressLint("ResourceType")
        fun ImageView.setImage64(base64: String?) {
            Glide.with(this.context.getCompatActivity()!!).load(decode(base64, DEFAULT))
                .transform(RoundedCorners(8)).into(this)
        }

        fun ImageView.setImageUrl(url: String){
            val circularProgressDrawable = CircularProgressDrawable(this.context)
            circularProgressDrawable.strokeWidth = 2f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide.with(this.context.getCompatActivity()!!).load(url).placeholder(circularProgressDrawable).into(this)
        }
        fun ImageView?.default(name: String?, @ColorRes color: Int) {
            val textDrawable = TextDrawable.builder()
                .beginConfig()
                .width(120)
                .height(120)
                .bold()
                .toUpperCase()
                .textColor(color)
                .endConfig()
                .buildRound(name?.substring(0, 1), Color.WHITE)
            this?.setImageDrawable(textDrawable)
        }

        fun ImageView?.defaultMaterial(name: String?) {
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
            this?.setImageDrawable(textDrawable)
        }

        fun ImageView.toBase64(): String? {
            return try {
                val byteArrayOutputStream = ByteArrayOutputStream()
                (this.drawable as BitmapDrawable).bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                encodeToString(byteArray, DEFAULT)
            } catch (e: Exception) { "" }
        }

        fun ImageView.tint(@ColorRes colorRes: Int) {
            ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(this.context, colorRes)))
        }

        fun ImageView.setPattern(@DrawableRes drawableRes: Int){
            this.setImageDrawable(TileDrawable(ContextCompat.getDrawable(this.context.getCompatActivity()!!, drawableRes)!!, Shader.TileMode.REPEAT))
        }
    }
}