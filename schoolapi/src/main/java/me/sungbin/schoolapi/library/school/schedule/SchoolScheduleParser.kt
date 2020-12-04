package me.sungbin.schoolapi.library.school.schedule


import me.sungbin.schoolapi.library.school.SchoolException
import me.sungbin.schoolapi.library.util.StringUtil
import java.util.*

object SchoolScheduleParser {

    @Throws(SchoolException::class)
    fun parse(_rawData: String): List<SchoolSchedule> {
        if (_rawData.isEmpty()) throw SchoolException("Data is Empty.")

        var rawData = _rawData
        rawData = rawData.replace("\\s+".toRegex(), "")

        val monthlySchedule = ArrayList<SchoolSchedule>()
        val chunk =
            rawData.split("textL\">".toRegex()).dropLastWhile { it.isEmpty() }.toList()

        try {
            for (i in 1 until chunk.size) {
                val schedule = StringBuilder()
                var trimmed = StringUtil.before(chunk[i], "</div>")
                val date = StringUtil.before(StringUtil.after(trimmed, ">"), "</em>")

                if (date.isEmpty()) continue

                while (trimmed.contains("<strong>")) {
                    val name = StringUtil.before(StringUtil.after(trimmed, "<strong>"), "</strong>")
                    schedule.append(name)
                    schedule.append("\n")
                    trimmed = StringUtil.after(trimmed, "</strong>")
                }

                monthlySchedule.add(SchoolSchedule(schedule.toString()))
            }
            return monthlySchedule
        } catch (exception: Exception) {
            throw SchoolException("Error at Parsing!\n\n${exception.localizedMessage}")
        }
    }

}