package com.acdev.commonFunction.util.view

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
import androidx.core.view.ViewCompat
import androidx.core.view.setMargins
import androidx.viewpager.widget.ViewPager
import com.acdev.commonFunction.common.SocialMedia
import com.acdev.commonFunction.common.Toast
import com.acdev.commonFunction.util.Functionx
import com.acdev.commonFunction.util.Functionx.Companion.getCompatActivity
import com.acdev.commonFunction.util.Toast.Companion.toast
import com.acdev.commonFunction.util.DataType
import com.acdev.commonFunction.util.DataType.Companion.add62
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.h6ah4i.android.tablayouthelper.TabLayoutHelper

class OtherView {
    companion object{
        fun View.layoutTint(@ColorRes colorRes: Int) {
            ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
        }

        fun View.setMargin(margin: Int) {
            if (this.layoutParams is ViewGroup.MarginLayoutParams) {
                val p = this.layoutParams as ViewGroup.MarginLayoutParams
                p.setMargins(margin)
                this.requestLayout()
            }
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun View.backgroundTint(@ColorRes colorRes: Int) {
            this.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
        }

        fun MaterialCardView.nestedClick(view: View) {
            this.setOnClickListener { if (view.visibility == Functionx.gone) view.visibility =
                Functionx.visible else view.visibility = Functionx.gone
            }
        }

        fun TextView.html(foo: String){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) this.text = Html.fromHtml(foo, Html.FROM_HTML_MODE_COMPACT)
            else this.text = Html.fromHtml(foo)
        }

        fun TabLayout.setupWithViewPagerHelper(viewPager: ViewPager){
            this.setupWithViewPager(viewPager)
            TabLayoutHelper(this, viewPager).isAutoAdjustTabModeEnabled = true
        }

        fun TextView.showVersion(){
            try { this.text = "Versi ${this.context.getCompatActivity()!!.packageManager.getPackageInfo(this.context.getCompatActivity()!!.packageName, 0).versionName}" }
            catch (e: PackageManager.NameNotFoundException) { this.context.getCompatActivity()!!.toast(
                Toast.WARNING, e.message.toString()) }
        }

        fun View.shareVia(appName: String, packageName: String){
            this.setOnClickListener { ShareCompat.IntentBuilder.from(this.context as Activity).setType("text/plain")
                .setChooserTitle("Bagikan Menggunakan").setText("Unduh aplikasi $appName secara gratis! Silahkan unduh di " +
                        "https://play.google.com/store/apps/details?id=$packageName").startChooser()
            }
        }

        fun View.socialMediaOnclick(data: String, socialMedia: SocialMedia){
            this.setOnClickListener {
                when(socialMedia){
                    SocialMedia.FACEBOOK -> {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse("https://$data")
                        this.context.startActivity(i)
                    }
                    SocialMedia.INSTAGRAM -> {
                        val uri = Uri.parse("http://instagram.com/_u/${data.replace("www.instagram.com/","")}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.setPackage("com.instagram.android")
                        try { this.context.startActivity(intent) } catch (e: ActivityNotFoundException) {
                            this.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://$data")))
                        }
                    }
                    SocialMedia.WHATSAPP -> {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse("https://api.whatsapp.com/send?phone=${data.add62()}")
                        this.context.startActivity(i)
                    }
                    SocialMedia.GMAIL -> {
                        try { this.context.startActivity(Intent(Intent.ACTION_VIEW , Uri.parse("mailto:$data"))) }
                        catch(e: ActivityNotFoundException){}
                    }
                }
            }
        }


    }
}