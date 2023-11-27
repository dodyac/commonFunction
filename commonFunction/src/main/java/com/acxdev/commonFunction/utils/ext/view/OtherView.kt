package com.acxdev.commonFunction.utils.ext.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ShareCompat
import androidx.core.view.ViewCompat
import androidx.core.view.get
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.acxdev.commonFunction.model.SocialMedia
import com.acxdev.commonFunction.utils.ext.emptyString
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.gson.Gson

fun View.layoutTint(@ColorInt color: Int) {
    ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(color))
}

fun View.setMargin(margin: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(margin)
        requestLayout()
    }
}

fun View.backgroundTint(@ColorInt color: Int) {
    backgroundTintList = ColorStateList.valueOf(color)
}

fun TextView.setHtml(string: String) {
    text = Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT)
}

fun View.shareVia(
    chooserTitle: String,
    contentText: String,
) {
    setOnClickListener {
        ShareCompat.IntentBuilder(context).setType("text/plain")
            .setChooserTitle(chooserTitle).setText(
                contentText + "https://play.google.com/store/apps/details?id=${context.packageName}"
            ).startChooser()
    }
}

fun View.socialMediaOnclick(data: String, socialMedia: SocialMedia) {
    setOnClickListener {
        when (socialMedia) {
            SocialMedia.Facebook -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://$data")
                context.startActivity(i)
            }
            SocialMedia.Instagram -> {
                val uri = Uri.parse(
                    "http://instagram.com/_u/${
                        data.replace(
                            "www.instagram.com/",
                            emptyString()
                        )
                    }"
                )
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.instagram.android")
                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://$data")
                        )
                    )
                }
            }
            SocialMedia.Whatsapp -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://api.whatsapp.com/send?phone=${data}")
                context.startActivity(i)
            }
            SocialMedia.Gmail -> {
                try {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("mailto:$data")
                        )
                    )
                } catch (_: ActivityNotFoundException) {
                }
            }
        }
    }
}

fun ViewPager2.setWrapContent() {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val view =
                (this@setWrapContent[0] as RecyclerView).layoutManager?.findViewByPosition(
                    position
                )

            view?.post {
                val wMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                val hMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                view.measure(wMeasureSpec, hMeasureSpec)

                if (layoutParams.height != view.measuredHeight) {
                    layoutParams =
                        (layoutParams).also { lp -> lp.height = view.measuredHeight }
                }
            }
        }
    })
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun TextView.leftDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
}

val Any.json: String
    get() = Gson().toJson(this)

fun SwipeRefreshLayout.whenRefreshed(
    delay: Long = 200,
    action: () -> Unit
) {
    setOnRefreshListener {
        postDelayed({
            isRefreshing = false
            action.invoke()
        }, delay)
    }
}

fun View.slideUp() {
    val layoutParams = layoutParams as CoordinatorLayout.LayoutParams
    val bottomViewNavigationBehavior = layoutParams.behavior as HideBottomViewOnScrollBehavior
    bottomViewNavigationBehavior.slideUp(this)
}