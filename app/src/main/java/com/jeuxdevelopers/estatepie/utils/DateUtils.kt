package com.jeuxdevelopers.estatepie.utils

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun getStringDateFromMillis(millis: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(cal.time)
    }

    fun getHolidayDateFromString(strDate: String): Date {
        // Timber.d("Holiday: Incoming String date -> $strDate")
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            val date = format.parse(strDate)
            //  Timber.d("Holiday: Date is -> ${format.format(date)}")
            date
        } catch (e: Exception) {
            Timber.d("Holiday: Date exception -> ${e.localizedMessage}")
            Date()
        }
    }

    fun getNextYearFirstDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1)
        calendar.set(Calendar.MONTH, 0)
        calendar.set(Calendar.DATE, 1)
        return calendar.time
    }

    fun getCurrentMonthWithYear(): String {
        val format = SimpleDateFormat("MMM yyyy", Locale.getDefault())
        return format.format(Calendar.getInstance().time)
    }

    private const val SIMPLE_DATE_FORMAT = "yyyy-MM-dd"
    private const val EXPIRY_DATE_FORMAT = "MM/YY"
    private const val DATE_FORMAT = "dd MMM yyyy"
    private const val DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm aa"
    private const val TIME_FORMAT = "hh:mm aa"
    private const val DATE_TIME_SECOND_FORMAT = "yyyy-MM-dd hh:mm:ss"
    private const val MONTH_FORMAT = "MM"
    private const val YEAR_FORMAT = "yy"

    fun getDateStringFromMillis(millis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        val format = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return format.format(calendar.time)
    }

    fun getSimpleDateStringFromMillis(millis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        val format = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault())
        return format.format(calendar.time)
    }

    fun getExpiryDateStringFromMillis(millis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        val format = SimpleDateFormat(EXPIRY_DATE_FORMAT, Locale.getDefault())
        return format.format(calendar.time)
    }

    fun getTimeFromMillis(millis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        val format = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        return format.format(calendar.time)
    }


    fun getMonthFromMillis(millis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        val format = SimpleDateFormat(MONTH_FORMAT, Locale.getDefault())
        return format.format(calendar.time)
    }

    fun getYearFromMillis(millis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        val format = SimpleDateFormat(YEAR_FORMAT, Locale.getDefault())
        return format.format(calendar.time)
    }

    fun getDateTimeFromMillis(millis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        val format = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        return format.format(calendar.time)
    }
    fun getDateTimeWithSecondFromMillis(millis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        val format = SimpleDateFormat(DATE_TIME_SECOND_FORMAT, Locale.getDefault())
        return format.format(calendar.time)
    }

    fun getTimeAgo(millis: Long): String {

        val calendar = Calendar.getInstance()
        calendar.time = Date(millis)

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val currentCalendar = Calendar.getInstance()

        val currentYear = currentCalendar.get(Calendar.YEAR)
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentCalendar.get(Calendar.MINUTE)

        return if (year < currentYear ) {
            val interval = currentYear - year
            if (interval == 1) "$interval year ago" else "$interval years ago"
        } else if (month < currentMonth) {
            val interval = currentMonth - month
            if (interval == 1) "$interval month ago" else "$interval months ago"
        } else  if (day < currentDay) {
            val interval = currentDay - day
            if (interval == 1) "$interval day ago" else "$interval days ago"
        } else if (hour < currentHour) {
            val interval = currentHour - hour
            if (interval == 1) "$interval hour ago" else "$interval hours ago"
        } else if (minute < currentMinute) {
            val interval = currentMinute - minute
            if (interval == 1) "$interval minute ago" else "$interval minutes ago"
        } else {
            "a moment ago"
        }
    }

}