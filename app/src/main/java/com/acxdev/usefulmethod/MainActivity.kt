package com.acxdev.usefulmethod

import android.graphics.Color
import com.acxdev.commonFunction.common.base.BaseActivityLib
import com.acxdev.commonFunction.util.ext.view.setImageUrl
import com.acxdev.commonFunction.util.toast
import com.acxdev.commonFunction.widget.asTabLayoutWith
import com.acxdev.usefulmethod.databinding.ActivityMainBinding
import com.acxdev.usefulmethod.databinding.RowTabLayoutBinding

class MainActivity : BaseActivityLib<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun ActivityMainBinding.configureViews() {
        val menuList = listOf(
            TabLayout("All", "https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AA16C5N.img?w=720&h=781&m=6"),
            TabLayout("News", randomImage()),
            TabLayout("Daily Quiz", randomImage()),
            TabLayout("Stereo", randomImage()),
            TabLayout("Radio", randomImage()),
            TabLayout("Bullish", randomImage()),
            TabLayout("Bearish", randomImage()),
            TabLayout("Rugpull", randomImage()),
            TabLayout("Eyes", randomImage()),
            TabLayout("Tight", randomImage()),
            TabLayout("Before", randomImage()),
            TabLayout("Game", randomImage()),
        ).toMutableList()

        supportActionBar?.hide()

        recycler.asTabLayoutWith(menuList, RowTabLayoutBinding::inflate) { vb, t, b ->
            vb.apply {
                image.setImageUrl(t.image, Color.RED)
                name.apply {
                    text = t.str
                    if (b) {
                        name.apply {
                            setTextColor(context.getColor(R.color.white))
                            setBackgroundResource(R.drawable.bg_fade_blue)
                        }
                    } else {
                        name.apply {
                            setTextColor(context.getColor(R.color.tabLayoutInactive))
                            setBackgroundResource(android.R.color.transparent)
                        }
                    }
                }
            }
        }
    }

    override fun ActivityMainBinding.onClickListener() {}

    private fun randomImage(): String {
        return listOf(
            "https://ravenpool.ninja/assets/images/logo-icon.png",
            "https://pool.space/favicon.ico",
            "https://www.leafpool.com/favicon.ico",
            "https://i.ibb.co/7JL6FLB/default.png",
            "https://www.digipools.org/img/fav/favicon-96x96.png",
            "https://i.ibb.co/7JL6FLB/default.png",
            "https://ezil.me/static/favicon.ico"
        ).random()
    }
}

data class TabLayout(val str: String, val image: String)