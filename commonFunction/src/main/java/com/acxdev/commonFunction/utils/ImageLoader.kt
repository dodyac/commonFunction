package com.acxdev.commonFunction.utils

import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.acxdev.commonFunction.utils.ImageLoader.setImage64
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

object ImageLoader {

    fun ImageView.setImageUrl(
        url: String?,
        imageStyle: ImageStyle = ImageStyle.None,
        @DrawableRes
        placeHolderDrawable: Int? = null,
        @DrawableRes
        errorDrawable: Int? = null
    ) {
        val glide = Glide.with(context)
            .load(url)

        placeHolderDrawable?.let {
            glide.placeholder(placeHolderDrawable)
        }
        errorDrawable?.let {
            glide.error(errorDrawable)
        }

        when(imageStyle) {
            ImageStyle.None -> {
                glide.into(this)
            }
            is ImageStyle.Rounded -> {
                glide.transform(RoundedCorners(imageStyle.radius))
                    .into(this)
            }
            ImageStyle.Circle -> {
                glide.transform(CircleCrop())
                    .into(this)
            }
        }
    }

    fun ImageView.setImage64(
        base64: String?,
        imageStyle: ImageStyle = ImageStyle.None,
        @DrawableRes
        placeHolderDrawable: Int? = null,
        @DrawableRes
        errorDrawable: Int? = null
    ) {
        val glide = Glide.with(context)
            .load(Base64.decode(base64, Base64.DEFAULT))

        placeHolderDrawable?.let {
            glide.placeholder(placeHolderDrawable)
        }
        errorDrawable?.let {
            glide.error(errorDrawable)
        }

        when(imageStyle) {
            ImageStyle.None -> {
                glide.into(this)
            }
            is ImageStyle.Rounded -> {
                glide.transform(RoundedCorners(imageStyle.radius))
                    .into(this)
            }
            ImageStyle.Circle -> {
                glide.transform(CircleCrop())
                    .into(this)
            }
        }
    }

    fun ImageView.setImageUri(
        uri: Uri?,
        imageStyle: ImageStyle = ImageStyle.None,
        @DrawableRes
        placeHolderDrawable: Int? = null,
        @DrawableRes
        errorDrawable: Int? = null
    ) {
        val glide = Glide.with(context)
            .load(uri)

        placeHolderDrawable?.let {
            glide.placeholder(placeHolderDrawable)
        }
        errorDrawable?.let {
            glide.error(errorDrawable)
        }

        when(imageStyle) {
            ImageStyle.None -> {
                glide.into(this)
            }
            is ImageStyle.Rounded -> {
                glide.transform(RoundedCorners(imageStyle.radius))
                    .into(this)
            }
            ImageStyle.Circle -> {
                glide.transform(CircleCrop())
                    .into(this)
            }
        }
    }


    sealed class ImageStyle {
        data object Circle: ImageStyle()
        data class Rounded(val radius: Int): ImageStyle()
        data object None: ImageStyle()
    }
}