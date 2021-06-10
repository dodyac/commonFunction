package com.acxdev.commonFunction.util.view

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.setMargins
import androidx.viewpager.widget.ViewPager
import com.acxdev.commonFunction.common.SocialMedia
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.Functionx
import com.acxdev.commonFunction.util.Functionx.Companion.getCompatActivity
import com.acxdev.commonFunction.util.Toast.Companion.toast
import com.acxdev.commonFunction.util.DataType.Companion.add62
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.h6ah4i.android.tablayouthelper.TabLayoutHelper

class OtherView {
    companion object{
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

        fun MaterialCardView.nestedClick(view: View) {
            setOnClickListener { if (view.visibility == Functionx.gone) view.visibility =
                Functionx.visible else view.visibility = Functionx.gone
            }
        }

        fun TextView.html(foo: String){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) text = Html.fromHtml(foo, Html.FROM_HTML_MODE_COMPACT)
            else text = HtmlCompat.fromHtml(foo, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        fun TabLayout.setupWithViewPagerHelper(viewPager: ViewPager){
            setupWithViewPager(viewPager)
            TabLayoutHelper(this, viewPager).isAutoAdjustTabModeEnabled = true
        }

        fun TextView.showVersion(){
            try { text = "Versi ${context.getCompatActivity()!!.packageManager.getPackageInfo(context.getCompatActivity()!!.packageName, 0).versionName}" }
            catch (e: PackageManager.NameNotFoundException) { context.getCompatActivity()!!.toast(
                Toast.WARNING, e.message.toString()) }
        }

        fun View.shareVia(appName: String, packageName: String){
            setOnClickListener { ShareCompat.IntentBuilder.from(context as Activity).setType("text/plain")
                .setChooserTitle("Bagikan Menggunakan").setText("Unduh aplikasi $appName secara gratis! Silahkan unduh di " +
                        "https://play.google.com/store/apps/details?id=$packageName").startChooser()
            }
        }

        fun View.socialMediaOnclick(data: String, socialMedia: SocialMedia){
            setOnClickListener {
                when(socialMedia){
                    SocialMedia.FACEBOOK -> {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse("https://$data")
                        context.startActivity(i)
                    }
                    SocialMedia.INSTAGRAM -> {
                        val uri = Uri.parse("http://instagram.com/_u/${data.replace("www.instagram.com/","")}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.setPackage("com.instagram.android")
                        try { context.startActivity(intent) } catch (e: ActivityNotFoundException) {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://$data")))
                        }
                    }
                    SocialMedia.WHATSAPP -> {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse("https://api.whatsapp.com/send?phone=${data.add62()}")
                        context.startActivity(i)
                    }
                    SocialMedia.GMAIL -> {
                        try { context.startActivity(Intent(Intent.ACTION_VIEW , Uri.parse("mailto:$data"))) }
                        catch(e: ActivityNotFoundException){}
                    }
                }
            }
        }
    }
}