package com.acxdev.commonFunction.util.ext.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.get
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.acxdev.commonFunction.common.SocialMedia
import com.acxdev.commonFunction.util.ext.emptyString
import com.acxdev.commonFunction.util.ext.getCompatActivity
import com.google.gson.Gson

fun View.layoutTint(@ColorRes colorRes: Int) {
    ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
}

fun View.setMargin(margin: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(margin)
        requestLayout()
    }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.backgroundTint(@ColorRes colorRes: Int) {
    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
}

fun TextView.setHtml(string: String) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT)
    else HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun View.shareVia(
    chooserTitle: String,
    contentText: String,
) {
    setOnClickListener {
        ShareCompat.IntentBuilder.from(context.getCompatActivity()).setType("text/plain")
            .setChooserTitle(chooserTitle).setText(
                contentText +
                        "https://play.google.com/store/apps/details?id=${context.packageName}"
            ).startChooser()
    }
}

fun View.socialMediaOnclick(data: String, socialMedia: SocialMedia) {
    setOnClickListener {
        when (socialMedia) {
            SocialMedia.FACEBOOK -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://$data")
                context.startActivity(i)
            }
            SocialMedia.INSTAGRAM -> {
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
            SocialMedia.WHATSAPP -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://api.whatsapp.com/send?phone=${data}")
                context.startActivity(i)
            }
            SocialMedia.GMAIL -> {
                try {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("mailto:$data")
                        )
                    )
                } catch (e: ActivityNotFoundException) {
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

fun View.visibleIf(state: Boolean, isInvisible: Boolean = false) {
    if(state) visible()
    else {
        if(isInvisible) invisible()
        else gone()
    }
}

fun TextView.leftDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
}

fun Any.toJson(): String {
    return Gson().toJson(this)
}

fun SwipeRefreshLayout.whenRefreshed(action: () -> Unit) {
    action.invoke()
    setOnRefreshListener {
        postDelayed({
            isRefreshing = false
            action.invoke()
        }, 2)
    }
}