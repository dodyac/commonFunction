package com.acxdev.commonFunction.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.core.view.isVisible
import androidx.transition.TransitionManager
import com.acxdev.commonFunction.R
import com.acxdev.commonFunction.utils.ext.view.gone
import com.acxdev.commonFunction.utils.ext.view.visible
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.button.MaterialButton

class EmptyLayoutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val lottieAnimationView: LottieAnimationView
    private val imageView: ImageView
    private val titleTextView: TextView
    private val bodyTextView: TextView
    private val materialButton: MaterialButton
    private var currentState = false

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_empty, this, true)

        lottieAnimationView = findViewById(R.id.lottie)
        imageView = findViewById(R.id.image)
        titleTextView = findViewById(R.id.title)
        bodyTextView = findViewById(R.id.body)
        materialButton = findViewById(R.id.button)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmptyLayout)

        val titleText = typedArray.getString(R.styleable.EmptyLayout_title)
        val bodyText = typedArray.getString(R.styleable.EmptyLayout_body)
        val imageResource = typedArray.getResourceId(R.styleable.EmptyLayout_image, 0)
        val lottieRawRes = typedArray.getResourceId(R.styleable.EmptyLayout_lottie, 0)
//        val buttonVisible = typedArray.getBoolean(R.styleable.EmptyLayout_isButtonVisible, false)
//        val buttonText = typedArray.getString(R.styleable.EmptyLayout_buttonText)
//        val buttonStyle = typedArray.getResourceId(R.styleable.EmptyLayout_buttonStyle, 0)

        if (lottieRawRes != 0) {
            setLottieRawRes(lottieRawRes)
        } else if (imageResource != 0) {
            setImage(imageResource)
        } else {
            lottieAnimationView.gone()
            imageView.gone()
        }

        titleTextView.text = titleText
        bodyTextView.text = bodyText

//        materialButton.isVisible = buttonVisible
//        materialButton.setTextAppearance(buttonStyle)
//        materialButton.text = buttonText

        typedArray.recycle()
    }

    fun setLayoutVisibility(visible: Boolean) {
        if (currentState != visible) {
            (parent as? ViewGroup)?.let {
                TransitionManager.beginDelayedTransition(it)
            }
            isVisible = visible
            if (visible) {
                lottieAnimationView.playAnimation()
            } else {
                lottieAnimationView.pauseAnimation()
            }
        }
        currentState = visible
    }

//    fun isButtonVisible(visible: Boolean) {
//        materialButton.isVisible = visible
//    }

    fun setTitle(title: String) {
        titleTextView.text = title
    }

    fun setBody(body: String) {
        bodyTextView.text = body
    }

    fun setImage(@DrawableRes image: Int) {
        imageView.setImageResource(image)
        imageView.visible()
        lottieAnimationView.gone()
    }

    fun setLottieRawRes(@RawRes rawRes: Int) {
        lottieAnimationView.apply {
            setAnimation(rawRes)
            visible()
            repeatCount = LottieDrawable.INFINITE
        }
        imageView.gone()
    }
}
