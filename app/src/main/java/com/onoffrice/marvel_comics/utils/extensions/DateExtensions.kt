package com.onoffrice.marvel_comics.utils.extensions

import com.onoffrice.marvel_comics.Constants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val SECOND_MILLIS = 1000f
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS
private const val MONTH_MILLIS = 30 * DAY_MILLIS

fun Date.inMillis(): Long{
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.timeInMillis
}

fun String.inMillis(dateFormat: String) =
        this.dateFromString(dateFormat)?.inMillis()


fun String.changeDateFormat(currentDateFormat: String, newDateFormat: String,
                            locale: Locale = Locale(Constants.LANGUAGE_PT, Constants.COUNTRY_BR), isUTC: Boolean = false): String {
    return try {
        val oldDf = SimpleDateFormat(currentDateFormat)
        val date = oldDf.parse(this)
        val newDf = SimpleDateFormat(newDateFormat)

        if (isUTC)
            newDf.timeZone = TimeZone.getTimeZone("gmt")

        newDf.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}


fun String.dateFromString(dateFormat: String, locale: Locale = Locale(Constants.LANGUAGE_PT, Constants.COUNTRY_BR),
                          isUTC: Boolean = false): Date? {
    return try {
        val date = if (isUTC) changeDateFormat(dateFormat, dateFormat, isUTC = true) else this
        val sdf = SimpleDateFormat(dateFormat, locale)
        sdf.parse(date)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


fun Date.isToday(): Boolean{
    val today = Calendar.getInstance()
    val selected = Calendar.getInstance()
    selected.time = this

    return today.get(Calendar.MONTH) == selected.get(Calendar.MONTH)
            && today.get(Calendar.YEAR) == selected.get(Calendar.YEAR)
            && today.get(Calendar.DAY_OF_MONTH) == selected.get(Calendar.DAY_OF_MONTH)
}

fun Date.isPast(): Boolean{
    val today = Calendar.getInstance()
    val selected = Calendar.getInstance()
    selected.time = this

    return today.after(selected)
}

@Throws(ParseException::class)
fun String.getYear(format: String): Int {
    val cal = Calendar.getInstance()
    cal.time = SimpleDateFormat(format).parse(this)
    return cal.get(Calendar.YEAR)
}

@Throws(ParseException::class)
fun String.getDay(format: String): Int {
    val cal = Calendar.getInstance()
    cal.time = SimpleDateFormat(format).parse(this)
    return cal.get(Calendar.DAY_OF_MONTH)
}

@Throws(ParseException::class)
fun String.getMonthNumber(format: String, startFromZero: Boolean): Int {
    val cal = Calendar.getInstance()
    cal.time = SimpleDateFormat(format).parse(this)
    return cal.get(Calendar.MONTH) + if(!startFromZero) 1 else 0
}

@Throws(ParseException::class)
fun String.getDate(format: String): Date? {
    val cal = Calendar.getInstance()
    cal.time = SimpleDateFormat(format).parse(this)
    return cal.time
}

fun Calendar.format(dateFormat: String, locale: Locale = Locale(Constants.LANGUAGE_PT, Constants.COUNTRY_BR)): String {
    val sdf = SimpleDateFormat(dateFormat, locale)
    return sdf.format(this.time)
}

fun Date.displayName(format: String): String{
    val cal = Calendar.getInstance()
    cal.time = this
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    return sdf.format(cal.time)
}

fun String.calendarFromString(dateFormat: String, locale: Locale = Locale(Constants.LANGUAGE_PT, Constants.COUNTRY_BR),
                              isUTC: Boolean = false): Calendar? {
    return try {
        val cal = Calendar.getInstance()
        if(isUTC)
            cal.timeZone = TimeZone.getTimeZone("gmt")
        val date = this.dateFromString(dateFormat, locale)
        cal.time = date
        return cal
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}



