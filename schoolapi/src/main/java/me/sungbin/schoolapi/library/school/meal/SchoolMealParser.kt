package me.sungbin.schoolapi.library.school.meal

import me.sungbin.schoolapi.library.school.SchoolException
import me.sungbin.schoolapi.library.util.toStringOrNull
import java.util.*
import kotlin.collections.HashMap

object SchoolMealParser {

    @Throws(SchoolException::class)
    fun parse(_rawData: String): List<SchoolMeal> {
        if (_rawData.isEmpty()) throw SchoolException("Data is Empty.")

        var rawData = _rawData
        rawData = rawData.replace("\\s+".toRegex(), "")

        val monthlyMenu = ArrayList<SchoolMeal>()
        val builder = StringBuilder()
        var inDiv = false

        try {
            for (i in rawData.indices) {
                if (rawData[i] == 'v') {
                    if (inDiv) {
                        builder.delete(builder.length - 4, builder.length)
                        if (builder.isNotEmpty()) monthlyMenu.add(parseDay(builder.toString()))
                        builder.setLength(0)
                    }
                    inDiv = !inDiv
                } else if (inDiv) {
                    builder.append(rawData[i])
                }
            }
            return monthlyMenu
        } catch (exception: Exception) {
            throw SchoolException("Error at Parsing!\n\n${exception.localizedMessage}")
        }
    }

    private fun parseDay(_rawData: String): SchoolMeal {
        var rawData = _rawData
        rawData = rawData.replace("(석식)", "")
        rawData = rawData.replace("(선)", "")

        val menu = SchoolMeal()
        val chunk = rawData.split("<br/>".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val menuStrings = HashMap<SchoolMealType, StringBuilder>()
        var parsingMode: SchoolMealType? = null

        for (i in 1 until chunk.size) {
            if (chunk[i].trim().isEmpty()) continue

            parsingMode = when (chunk[i]) {
                "[조식]" -> SchoolMealType.BREAKFAST
                "[중식]" -> SchoolMealType.LUNCH
                "[석식]" -> SchoolMealType.DINNER
                else -> parsingMode
            }

            if (menuStrings[parsingMode!!] == null) menuStrings[parsingMode] = StringBuilder()
            menuStrings[parsingMode]?.append(chunk[i] + "\n")
        }

        menu.breakfast = menuStrings[SchoolMealType.BREAKFAST].toStringOrNull()
        menu.lunch = menuStrings[SchoolMealType.LUNCH].toStringOrNull()
        menu.dinner = menuStrings[SchoolMealType.DINNER].toStringOrNull()

        return menu
    }
}