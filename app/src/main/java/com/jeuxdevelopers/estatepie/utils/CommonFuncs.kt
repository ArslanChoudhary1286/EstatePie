package com.jeuxdevelopers.estatepie.utils


class CommonFuncs {

    fun splitName(name: String): Pair<String?, String?> {
        val names = name.trim().split(Regex("\\s+"))
        return names.firstOrNull() to names.lastOrNull()
    }

}