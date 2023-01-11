package org.ovi.util.extensions

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern

fun String.isValidEmail(): Boolean = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()

/*fun String.isValidPassword() =
    this.isNotEmpty() &&
            *//*this.containsAtLeastEightDigit() && *//*(this.containsLowerCase() || this.containsUpperCase()) &&
            this.containsSpecialCase() &&
            this.containsNumber()*/

fun String.isValidPassword() =
    this.isNotEmpty() && containsAtLeastEightChars()

fun String.isAllDigitSame(): Boolean {
    val firstDigit = this[0]
    var isAllSame = false
    for (element in this) {
        isAllSame = (element == firstDigit)
        if (!isAllSame)
            break
    }
    return isAllSame
}

fun String.stripMobileNumber() = this.replace("[()\\s-]+".toRegex(), "")

fun String.isValidPhoneNumber(): Boolean {
    return stripMobileNumber().length == 10
}

fun String.isValidPhoneNumberWithoutStrip(): Boolean {
    return this.length == 14
}

fun String.isUsNumber() =
    !Pattern.compile("[()\\s-]+", Pattern.CASE_INSENSITIVE).matcher(this)
        .find()

fun String.containsDigitOnly(): Boolean = TextUtils.isDigitsOnly(this)

fun String.containsAtLeastSixChars() = this.length >= 6

fun String.containsAtLeastEightChars() = this.length >= 8

fun String.containsSpecialCase() =
    Pattern.compile("^(?=.*[!@#$&£_*?.,/{}<>]).+$", Pattern.CASE_INSENSITIVE).matcher(this)
        .find()

fun String.containsUpperCase(): Boolean {
    var ch: Char
    for (element in this) {
        ch = element
        if (Character.isUpperCase(ch)) {
            return true
        }
    }
    return false
}

fun String.containsLowerCase(): Boolean {
    var ch: Char
    for (element in this) {
        ch = element
        if (Character.isLowerCase(ch)) {
            return true
        }
    }
    return false
}


fun String.containsNumber(): Boolean {
    var ch: Char
    for (element in this) {
        ch = element
        if (Character.isDigit(ch)) {
            return true
        }

    }
    return false
}

fun String?.getInitials(): String {

    if (this.isNullOrEmpty())
        return ""

    return this.split(" ").filter {
        it.isNotEmpty()
    }.run {
        if (size > 1) {
            if (get(1)[0].toString().trim().isNotEmpty()) {
                get(0)[0].toString().plus(get(1)[0].toString())
            } else get(0)[0].toString()
        } else get(0)[0].toString()
    }

}