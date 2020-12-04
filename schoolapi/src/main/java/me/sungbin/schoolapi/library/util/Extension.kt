package me.sungbin.schoolapi.library.util


/**
 * Created by SungBin on 2020-12-04.
 */

fun StringBuilder?.toStringOrNull() = if (this.toString() == "null") null else this.toString()