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

    enum class Caching {
        ALL,
        NONE,
        DATA,
        RESOURCE,
        AUTOMATIC
    }

    suspend fun Context.loadImageAsBitmap(url: String, caching: Caching = Caching.AUTOMATIC): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val cacheStrategy = when(caching) {
                Caching.ALL -> DiskCacheStrategy.ALL // Cache both original and resized images
                Caching.NONE -> DiskCacheStrategy.NONE
                Caching.DATA -> DiskCacheStrategy.DATA
                Caching.RESOURCE -> DiskCacheStrategy.RESOURCE
                Caching.AUTOMATIC -> DiskCacheStrategy.AUTOMATIC
            }
            Glide.with(this@loadImageAsBitmap)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(cacheStrategy)
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

    fun ImageView.setImageGIF(gifUri: Uri?) {
        Glide.with(this)
            .asGif()
            .load(gifUri)
            .into(this)
    }

    fun ImageView.setImageUrl(
        url: String?,
        imageStyle: ImageStyle = ImageStyle.None,
        @DrawableRes
        placeHolderDrawable: Int? = null,
        @DrawableRes
        errorDrawable: Int? = null,
        isAnimating: Boolean = true,
        caching: Caching = Caching.AUTOMATIC
    ) {
        val cacheStrategy = when(caching) {
            Caching.ALL -> DiskCacheStrategy.ALL
            Caching.NONE -> DiskCacheStrategy.NONE
            Caching.DATA -> DiskCacheStrategy.DATA
            Caching.RESOURCE -> DiskCacheStrategy.RESOURCE
            Caching.AUTOMATIC -> DiskCacheStrategy.AUTOMATIC
        }

        val glide = Glide.with(context)
            .load(url)
            .diskCacheStrategy(cacheStrategy)

        placeHolderDrawable?.let {
            glide.placeholder(placeHolderDrawable)
        }
        errorDrawable?.let {
            glide.error(errorDrawable)
        }

        when(imageStyle) {
            ImageStyle.None -> {
                if (isAnimating) {
                    glide.into(this)
                } else {
                    glide.dontAnimate()
                        .into(this)
                }
            }
            is ImageStyle.Rounded -> {
                if (isAnimating) {
                    glide.transform(RoundedCorners(imageStyle.radius))
                        .into(this)
                } else {
                    glide.transform(RoundedCorners(imageStyle.radius))
                        .dontAnimate()
                        .into(this)
                }
            }
            ImageStyle.Circle -> {
                if (isAnimating) {
                    glide.transform(CircleCrop())
                        .into(this)
                } else {
                    glide.transform(CircleCrop())
                        .dontAnimate()
                        .into(this)
                }
            }
        }
    }

    fun ImageView.setImage64(
        base64: String?,
        imageStyle: ImageStyle = ImageStyle.None,
        @DrawableRes
        placeHolderDrawable: Int? = null,
        @DrawableRes
        errorDrawable: Int? = null,
        isAnimating: Boolean = true,
        caching: Caching = Caching.AUTOMATIC
    ) {
        val cacheStrategy = when(caching) {
            Caching.ALL -> DiskCacheStrategy.ALL
            Caching.NONE -> DiskCacheStrategy.NONE
            Caching.DATA -> DiskCacheStrategy.DATA
            Caching.RESOURCE -> DiskCacheStrategy.RESOURCE
            Caching.AUTOMATIC -> DiskCacheStrategy.AUTOMATIC
        }
        val glide = Glide.with(context)
            .load(Base64.decode(base64, Base64.DEFAULT))
            .diskCacheStrategy(cacheStrategy)

        placeHolderDrawable?.let {
            glide.placeholder(placeHolderDrawable)
        }
        errorDrawable?.let {
            glide.error(errorDrawable)
        }

        when(imageStyle) {
            ImageStyle.None -> {
                if (isAnimating) {
                    glide.into(this)
                } else {
                    glide.dontAnimate()
                        .into(this)
                }
            }
            is ImageStyle.Rounded -> {
                if (isAnimating) {
                    glide.transform(RoundedCorners(imageStyle.radius))
                        .into(this)
                } else {
                    glide.transform(RoundedCorners(imageStyle.radius))
                        .dontAnimate()
                        .into(this)
                }
            }
            ImageStyle.Circle -> {
                if (isAnimating) {
                    glide.transform(CircleCrop())
                        .into(this)
                } else {
                    glide.transform(CircleCrop())
                        .dontAnimate()
                        .into(this)
                }
            }
        }
    }

    fun ImageView.setImageUri(
        uri: Uri?,
        imageStyle: ImageStyle = ImageStyle.None,
        @DrawableRes
        placeHolderDrawable: Int? = null,
        @DrawableRes
        errorDrawable: Int? = null,
        isAnimating: Boolean = true,
        caching: Caching = Caching.AUTOMATIC
    ) {
        val cacheStrategy = when(caching) {
            Caching.ALL -> DiskCacheStrategy.ALL
            Caching.NONE -> DiskCacheStrategy.NONE
            Caching.DATA -> DiskCacheStrategy.DATA
            Caching.RESOURCE -> DiskCacheStrategy.RESOURCE
            Caching.AUTOMATIC -> DiskCacheStrategy.AUTOMATIC
        }
        val glide = Glide.with(context)
            .load(uri)
            .diskCacheStrategy(cacheStrategy)

        placeHolderDrawable?.let {
            glide.placeholder(placeHolderDrawable)
        }
        errorDrawable?.let {
            glide.error(errorDrawable)
        }

        when(imageStyle) {
            ImageStyle.None -> {
                if (isAnimating) {
                    glide.into(this)
                } else {
                    glide.dontAnimate()
                        .into(this)
                }
            }
            is ImageStyle.Rounded -> {
                if (isAnimating) {
                    glide.transform(RoundedCorners(imageStyle.radius))
                        .into(this)
                } else {
                    glide.transform(RoundedCorners(imageStyle.radius))
                        .dontAnimate()
                        .into(this)
                }
            }
            ImageStyle.Circle -> {
                if (isAnimating) {
                    glide.transform(CircleCrop())
                        .into(this)
                } else {
                    glide.transform(CircleCrop())
                        .dontAnimate()
                        .into(this)
                }
            }
        }
    }

    sealed class ImageStyle {
        data object Circle: ImageStyle()
        data class Rounded(val radius: Int): ImageStyle()
        data object None: ImageStyle()
    }
}