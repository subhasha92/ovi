package org.ovi.util.extensions

import android.util.Log
import java.io.File
import java.net.URLEncoder


fun String.firstLetterCaps(): String {
    val sb = StringBuilder(this)
    sb.setCharAt(0, Character.toUpperCase(sb[0]))
    return sb.toString()
}

fun String.removeFirstCharacter() = substring(1)

fun String?.numOfWords(): Int {
    if (this == null)
        return 0
    return split(Regex("\\s")).toTypedArray().size
}


fun String.addEllipse(size: Int): String {
    return if (this.length > size)
        String.format("%s...", this.trim().substring(0, 14))
    else this
}


//remove last comma character from string
fun String?.trimOutEscape(): String {

    if (this == null)
        return ""

    var returnStr = trim()
    if (returnStr.isNotEmpty() && returnStr[returnStr.length - 1] == ',') {
        returnStr = substring(0, returnStr.length - 1)
    }

    /* if (returnStr.contains(System.getProperty("line.separator"))) {
         returnStr = returnStr.replace(System.getProperty("line.separator"), "")
     }*/

    return returnStr
}

fun String.getNotesTitle(): String {

    val stringBuilder = StringBuilder()

    val splittedArr = trim().split(Regex("\\s")).toTypedArray()
    stringBuilder.append(
        when (splittedArr.size) {
            0 -> ""
            1 -> splittedArr[0]
            else -> splittedArr[0].plus(" ").plus(splittedArr[1])
        }
    )
    return stringBuilder.toString()
}

fun String.encode() = replace(" ", "_")

fun String.stripPhoneNumber(): String {

    val charList = this.toMutableList()
    val pickedList = arrayListOf<Char>()

    charList.forEachIndexed { index, char ->
        if (shouldAdd(pickedList.size, char)) {
            pickedList.add(char)
        }
    }
    //Log.d("StringXX", "stripPhoneNumber: finalCrap ${String(arr)}")
    return String(pickedList.toCharArray())
}

fun String?.hasValidFile(): Boolean {
    if (this == null)
        return false
    val file = File(this)
//    Log.d("StringExt", "hasValidFile: fileExt ${file.extension}")
    return file.exists() && file.length() > 0 && file.extension != "txt"
}

fun String.isValidImage() = (endsWith(".png", true) ||
        endsWith(".jpg", true) ||
        endsWith(".jpeg", true) ||
        endsWith(".heic", true) ||
        endsWith(".heif", true) ||
        endsWith(".webp", true))

fun String.stripExtension(): String {
    if (this.isNotEmpty() && this.indexOf(".") > 0)
        return this.substring(0, this.lastIndexOf("."))
    return this
}

fun String.hasExtension() = this.indexOf(".") > 0

fun String.urlEncoded(): String? = URLEncoder.encode(this, "utf-8")

fun String.removeAllWhiteSpaces() = apply { this.replace(" ", "") }


fun shouldAdd(size: Int, char: Char): Boolean {
    val array = charArrayOf('0', '1')
    if (Character.isDigit(char)) {
        var exists = false
        for (c in array) {
            if (c == char) {
                exists = true
                break
            }
        }
        return if (exists) size > 0 else true
    }
    return false
}
