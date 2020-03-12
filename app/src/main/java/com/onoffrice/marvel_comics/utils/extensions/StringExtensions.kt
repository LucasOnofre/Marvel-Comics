package com.onoffrice.marvel_comics.utils.extensions

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.annotation.StringRes
import com.onoffrice.marvel_comics.utils.TextMask
import com.onoffrice.marvel_comics.Constants
import java.text.NumberFormat
import java.util.*

/**
 * Returns string of float value to percentage
 * @param maximumFractionDigits length of fraction digits
 * @return percentage value of @value
 */
fun Float.toPercentage(maximumFractionDigits: Int? = null, minimumFractionDigits: Int? = null): String {
    val number = NumberFormat.getPercentInstance()
    maximumFractionDigits?.let { number.maximumFractionDigits = it }
    minimumFractionDigits?.let { number.minimumFractionDigits = minimumFractionDigits }
    return number.format(this.toDouble())
}

/**
 * @param locale locale of the currency to format
 * @return the value with the locale currency
 */
fun Float.formatToCurrency(locale: Locale): String {
    val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
    return currencyFormatter.format(this.toDouble())
}

fun Double.formatToCurrency(locale: Locale): String {
    val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
    return currencyFormatter.format(this)
}

/**
 * @return the value with the BRL currency
 */
fun Float.formatCurrencyBRL(trimRs: Boolean = false): String {
    val locale = Locale(Constants.LANGUAGE_PT, Constants.COUNTRY_BR)
    return if (trimRs) this.formatToCurrency(locale).replace("R$", "") else this.formatToCurrency(locale)
}

fun Long.formatCurrencyBRL(trimRs: Boolean = false): String {
    val locale = Locale(Constants.LANGUAGE_PT, Constants.COUNTRY_BR)
    val string = (this.toDouble() / 100.0).formatToCurrency(locale)
    return if (trimRs) string.replace("R$", "") else string
}

/**
 * @param text
 * @return text with the first letter capitalized
 */
fun String.capitalizeFirstLetter(): String {
    return this.substring(0, 1).capitalize() + this.substring(1)
}

fun String.getFloatValue() = this.replace("R$", "").replace(".", "").replace(",", ".").trim().toFloat()

fun Context.str(@StringRes id: Int): String {
    return getString(id)
}

fun String.colored(textToColor: String, color: Int, bold: Boolean = false): SpannableString {
    val spannable = SpannableString(this)

    val startIndex = this.indexOf(textToColor)
    if (startIndex >= 0) {
        val endIndex = startIndex + textToColor.length
        spannable.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        if (bold) {
            val style = StyleSpan(Typeface.BOLD)
            spannable.setSpan(style, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    return spannable
}

fun String.getCreditCardType(): CreditCardType {
    val regVisa = Regex("^4[0-9]{12}(?:[0-9]{3})?\$")
    val regMaster = Regex("^5[1-5][0-9]{14}\$")
    val regExpress = Regex("^3[47][0-9]{13}\$")
    val regDiners = Regex("^3(?:0[0-5]|[68][0-9])[0-9]{11}\$")
    val regDiscover = Regex("^6(?:011|5[0-9]{2})[0-9]{12}\$")
    val regJCB = Regex("^(?:2131|1800|35\\d{3})\\d{11}\$")


    return when {
        regVisa.matches(this) -> CreditCardType.Visa
        regMaster.matches(this) -> CreditCardType.Mastercard
        regExpress.matches(this) -> CreditCardType.AmericanExpress
        regDiners.matches(this) -> CreditCardType.Diners
        regDiscover.matches(this) -> CreditCardType.Discovers
        regJCB.matches(this) -> CreditCardType.JCB
        else -> CreditCardType.None
    }
}

fun String.setHighlightedColor(color: Int, isHtmlText:Boolean = false, substringToColor: String? = null, ignoreCase: Boolean = true, allMatches: Boolean = true): Spannable {
    val spannable = if(isHtmlText) SpannableString(this.fromHtml()) else SpannableString(this)
    if(!spannable.isBlank()) {
        if(!substringToColor.isNullOrBlank()) {
            var finalIndex = 0
            var areIndexesCorrect = true
            while (areIndexesCorrect) {
                val initialIndex = spannable.indexOf(substringToColor, finalIndex, ignoreCase)
                finalIndex = initialIndex + substringToColor.length
                areIndexesCorrect = initialIndex >= 0 && finalIndex < spannable.length
                if(areIndexesCorrect) {
                    spannable.setSpan(BackgroundColorSpan(color), initialIndex, finalIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                if(!allMatches) break
            }
        } else {
            spannable.setSpan(BackgroundColorSpan(color), 0, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
    return spannable
}


fun String.fromHtml() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
} else {
    Html.fromHtml(this)
}


fun TextView.setHtmlText(@StringRes stringRes: Int) {
    setHtmlText(context.getString(stringRes))
}

fun TextView.setHtmlText(text: String) {
    this.text = text.fromHtml()
}

fun String.unmask() = TextMask.unmask(this)

enum class CreditCardType {
    Visa, Mastercard, AmericanExpress, Diners, Discovers, JCB, None
}

fun <T> List<T>.concatList(): String {
    var result = ""
    this.forEach { result += it.toString() }
    return result
}

fun String.fillWithChars(minLenght: Int, char: Char, position: CustomString): String {
    if (this.length >= minLenght)
        return this

    val diff = minLenght - this.length
    var insert = (0 until diff).map { char }.concatList()

    return when (position) {
        CustomString.BEFORE -> {
            "$insert$this"
        }
        CustomString.AFTER -> {
            "$this$insert"
        }
        else -> {
            ""
        }
    }
}

fun String.addMask(mask: String, substitute: Char? = '#'): String{
    var index = 0
    var result = ""
    mask.forEach{ item ->
        if(index < this.length) {
            if(item == substitute) {
                result += this[index]
                index++
            } else {
                result += item
            }
        }
    }
    return result
}

enum class CustomString {
    BEFORE, AFTER, MID
}