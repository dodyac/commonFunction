package com.acxdev.usefulmethod

import android.graphics.Color
import com.acxdev.commonFunction.common.base.BaseActivity
import com.acxdev.commonFunction.model.ChoiceType
import com.acxdev.commonFunction.model.MultiChoice
import com.acxdev.commonFunction.utils.ext.view.asMultiChoice
import com.acxdev.usefulmethod.databinding.ActivityMainBinding
import com.acxdev.usefulmethod.databinding.RowTBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun ActivityMainBinding.configureViews() {

        val list = listOf(
            "CfY",
            "GL9w54s",
            "YnY6E",
            "5y3h1",
            "wKP",
            "ufU1Aw",
            "JBcL73",
            "8r6d3r9v",
            "e7UBtT",
            "zEaL845",
            "o1P",
            "Sc9IlHh",
            "zvai",
            "bRgoxq",
            "W94",
            "5CrVe1"
        )

        val choice = list.map {
            MultiChoice(it, false)
        }

        recycler.asMultiChoice(ChoiceType.Flex, choice, RowTBinding::inflate) { b, data ->
            if (b) {
                root.setTextColor(Color.WHITE)
            } else {
                root.setTextColor(Color.CYAN)
            }
            root.text = data.toString()
        }
    }
}
