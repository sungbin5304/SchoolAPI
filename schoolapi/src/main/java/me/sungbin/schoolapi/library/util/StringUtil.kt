package me.sungbin.schoolapi.library.util


object StringUtil {
    fun before(string: String, delimiter: String): String {
        val index = string.indexOf(delimiter)
        return string.substring(0, index)
    }

    fun after(string: String, delimiter: String): String {
        val index = string.indexOf(delimiter)
        return string.substring(index + delimiter.length)
    }
}