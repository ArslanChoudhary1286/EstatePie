package com.jeuxdevelopers.estatepie.utils.extensions

import android.content.Context
import androidx.annotation.StringRes

class ResourceUtility constructor(
    private val context: Context
) {

    fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }
}