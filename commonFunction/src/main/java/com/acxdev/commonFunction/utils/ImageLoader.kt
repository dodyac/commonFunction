package com.acxdev.commonFunction.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

object ImageLoader {

    suspend fun Context.loadImageAsBitmap(url: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            Glide.with(this@loadImageAsBitmap)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache both original and resized images
                .submit()
                .get()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun Context.loadImagesAsBitmap(urls: List<String>): List<Pair<String, Bitmap?>> = coroutineScope {
        urls.map { url ->
            async { Pair(url, loadImageAsBitmap(url)) }
        }.awaitAll()
    }

    fun ImageView.clearImage() {
        Glide.with(this)
            .clear(this)
    }

    @Deprecated("use setImage instead")
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