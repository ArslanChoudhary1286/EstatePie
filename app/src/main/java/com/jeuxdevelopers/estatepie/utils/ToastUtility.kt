package com.jeuxdevelopers.estatepie.utils;

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.jeuxdevelopers.estatepie.R
import io.github.muddz.styleabletoast.StyleableToast

object ToastUtility {

    fun errorToast(context: Context, resource: Int) {

        StyleableToast.Builder(context).textSize(12f).text(context.getString(resource))
            .backgroundColor(ContextCompat.getColor(context, R.color.red)).solidBackground()
            .textColor(Color.WHITE).cornerRadius(8).font(R.font.roboto_regular).show()
    }

    fun successToast(context: Context, resource: Int) {

        StyleableToast.Builder(context).textSize(12f).text(context.getString(resource))
            .backgroundColor(ContextCompat.getColor(context, R.color.blue)).solidBackground()
            .textColor(Color.WHITE).cornerRadius(8).font(R.font.roboto_regular).show()
    }

    fun errorToast(context: Context, message: String?) {

        StyleableToast.Builder(context).text(message).textSize(12f).textColor(Color.WHITE)
            .backgroundColor(ContextCompat.getColor(context, R.color.red)).solidBackground()
            .cornerRadius(8).font(R.font.roboto_regular).show()
    }

    fun successToast(context: Context, message: String?) {

        StyleableToast.Builder(context).text(message).textSize(12f).textColor(Color.WHITE)
            .backgroundColor(ContextCompat.getColor(context, R.color.blue)).solidBackground()
            .cornerRadius(8).font(R.font.roboto_regular).show()
    }
}
