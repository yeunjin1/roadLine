package com.lgcns.crossdev.onboarding1.presentation.util.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.lgcns.crossdev.onboarding1.presentation.util.extension.totalMinToHour
import com.lgcns.crossdev.onboarding1.presentation.util.extension.totalMinToMin
import com.lgcns.crossdev.onboarding1.presentation.util.extension.totalMinToString
import java.time.LocalDate

object ViewBindingAdapter {
    @BindingAdapter("setVisible")
    @JvmStatic
    fun View.setVisible(isVisible: Boolean){
        this.visibility = if(isVisible) View.VISIBLE else View.INVISIBLE
    }

    @BindingAdapter("setImage")
    @JvmStatic
    fun ImageView.setImage(url: String?){
        url?.let {
            Glide.with(this.context)
                .load(it)
                .into(this)
        }
    }
}