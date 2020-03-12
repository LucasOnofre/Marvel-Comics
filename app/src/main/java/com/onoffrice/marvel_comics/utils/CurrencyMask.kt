package com.onoffrice.marvel_comics.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat
import java.util.*

object CurrencyMask {

    fun unmask(string: String): String {
        return string.replace("[R$�,.()]".toRegex(), "")
    }

    fun parseValue(string: String): Float {
        return unmask(string).toFloat() / 100
    }

    fun insert(locale: Locale, editText: EditText, displayCurrency: Boolean): TextWatcher {
        return object : TextWatcher {
            var isUpdating: Boolean = false
            var old = ""

            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                if (s.toString() != old) {

                    isUpdating = true

                    val cleanString = unmask(s.toString())

                    try {
                        val parsed = java.lang.Double.parseDouble(cleanString.replace("[^0-9]".toRegex(), ""))
                        val formated = NumberFormat.getCurrencyInstance(locale)
                                .format(parsed / 100)

                        setFormatedValue(formated)
                    } catch (e: NumberFormatException) {

                        val formated = NumberFormat.getCurrencyInstance(locale)
                                .format(0)
                        setFormatedValue(formated)
                        e.printStackTrace()
                    }

                }

                // is erasing textRes
                if (old.length > s.length && s.length > 0) {
                    old = s.toString()
                    return
                }
            }

            private fun setFormatedValue(formated: String) {
                var formated = formated
                try {
                    formated = formated.replace("R$", "R$ ")
                    old = formated
                    editText.setText(formated)
                    editText.setSelection(formated.length)
                } catch (e: Exception) {
                }

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable) { }
        }
    }
}