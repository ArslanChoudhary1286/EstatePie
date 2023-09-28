package com.jeuxdevelopers.estatepie.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

object ExtensionUtils {

    fun <T> AppCompatActivity.collectLatestLifecycleFlow(
        flow: Flow<T>,
        collect: suspend (T) -> Unit
    ) {
        lifecycleScope.launchWhenStarted {
            flow.collectLatest(collect)
        }

    }


    fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                flow.collectLatest(collect)
            }
//            flow.collectLatest(collect)
        }
    }


//    inline fun <reified T : Any> T.asMap() : Map<String, Any?> {
//        val props = T::class.members.associateBy { it.name }
//        return props.keys.associateWith {
//           val d = props[it]?.parameters.get(this)
//           return@associateWith d.parameters.get(this@associateWith)
//        }
//    }


    @SuppressLint("HardwareIds")
    fun Context.toDeviceToken() = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)!!


    fun EditText.validateOnChange(
        checkAndValidate: (text: String) -> Unit
    ) {
        addTextChangedListener {
            doOnTextChanged { text, _, _, _ ->
                text?.let {
                    if (it.isNotEmpty()) checkAndValidate.invoke(it.toString())
                }
            }
        }
    }
}